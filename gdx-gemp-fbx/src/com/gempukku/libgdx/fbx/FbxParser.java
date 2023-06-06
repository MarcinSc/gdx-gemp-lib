package com.gempukku.libgdx.fbx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gempukku.libgdx.fbx.handler.*;
import com.gempukku.libgdx.fbx.handler.connection.ConnectionsRecordHandler;
import com.gempukku.libgdx.fbx.handler.objects.animation.AnimationLayerRecordHandler;
import com.gempukku.libgdx.fbx.handler.objects.animation.AnimationStackRecordHandler;
import com.gempukku.libgdx.fbx.handler.objects.attribute.NodeAttributeRecordHandler;
import com.gempukku.libgdx.fbx.handler.objects.geometry.GeometryRecordHandler;
import com.gempukku.libgdx.fbx.handler.objects.material.MaterialRecordHandler;
import com.gempukku.libgdx.fbx.handler.objects.model.ModelRecordHandler;
import com.gempukku.libgdx.fbx.handler.objects.texture.TextureRecordHandler;
import com.gempukku.libgdx.fbx.stream.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.InflaterInputStream;

public class FbxParser {
    private final static byte[] binaryHeader = "Kaydara FBX Binary  \u0000".getBytes(StandardCharsets.US_ASCII);

    private final byte[] readBuffer;
    private final int bufferSize;
    private BufferedInputStream inputStream;
    private int bytesRead;
    private int fileSize;
    private int recordDepth;

    public FbxParser() {
        this(1024);
    }

    public FbxParser(int bufferSize) {
        this.bufferSize = bufferSize;
        readBuffer = new byte[1024];
    }

    public void parse(FileHandle fileHandle, FbxHandler fbxHandler) {
        try (InputStream is = fileHandle.read()) {
            parse(is, fbxHandler, (int) fileHandle.length());
        } catch (Exception exp) {
            throw new GdxRuntimeException("Unable to parse FBX", exp);
        }
    }

    public void parse(InputStream inputStream, FbxHandler fbxHandler, int fileSize) {
        try {
            this.inputStream = new BufferedInputStream(inputStream, bufferSize);
            this.fileSize = fileSize;
            bytesRead = 0;
            parseWithExceptions(fbxHandler);
        } catch (Exception exp) {
            throw new GdxRuntimeException("Unable to parse FBX", exp);
        }
    }

    private void parseWithExceptions(FbxHandler fbxHandler) throws IOException {
        inputStream.mark(21);
        doFillBuffer(inputStream, readBuffer, 21);
        if (checkBinaryHeader(readBuffer)) {
            parseBinaryWithExceptions(fbxHandler);
        } else {
            inputStream.reset();
            bytesRead = 0;
            parseTextWithExceptions(fbxHandler);
        }
    }

    private boolean checkBinaryHeader(byte[] bytes) {
        for (int i = 0; i < 21; i++) {
            if (bytes[i] != binaryHeader[i])
                return false;
        }
        return true;
    }

    private void parseBinaryWithExceptions(FbxHandler fbxHandler) throws IOException {
        // Two fixed bytes
        doSkip(inputStream, 2);
        // Read version
        int version = readUint32(inputStream, readBuffer);

        try {
            while (bytesRead < fileSize) {
                parseRecord(fbxHandler);
            }
        } catch (Exception exp) {
            // Turns out most binary FBX contain some garbage at the end of the file.
            // Maybe some proprietary information for use by the editor, therefore
            // we just end the parsing, as if nothing happened on any error,
            // trusting that we've read all we could.

            // If record depth is greater than 0, just log a warning...
            if (recordDepth > 0) {
                Gdx.app.error("FBX Parser", "Read some garbage, but seemed to be inside a record", exp);
            }
        }
    }

    private void parseTextWithExceptions(FbxHandler fbxHandler) throws IOException {
        throw new UnsupportedOperationException("Text format FBX is not implemented");
    }

    private void parseRecord(FbxRecordContainer recordContainer) throws IOException {
        int endOffset = readUint32(inputStream, readBuffer);
        int numProperties = readUint32(inputStream, readBuffer);
        int propertyListLen = readUint32(inputStream, readBuffer);
        int nameLen = readUint8t(inputStream, readBuffer);
        if (endOffset != 0) {
            if (endOffset > fileSize || endOffset <= bytesRead || nameLen < 0 || propertyListLen < 0) {
                // That's the end of the file, we're reading in some garbage at this point
                return;
            }
        }
        String name = readString(inputStream, readBuffer, nameLen);
        if (endOffset > 0) {
            recordDepth++;
            FbxRecordHandler recordHandler = recordContainer.newRecord(name);
            if (recordHandler != null) {
                for (int i = 0; i < numProperties; i++) {
                    char propertyTypeCode = readChar(inputStream, readBuffer);
                    if (propertyTypeCode == 'Y') {
                        short valueShort = readShort(inputStream, readBuffer);
                        recordHandler.propertyValueShort(valueShort);
                    } else if (propertyTypeCode == 'C') {
                        boolean valueBoolean = readBoolean(inputStream, readBuffer);
                        recordHandler.propertyValueBoolean(valueBoolean);
                    } else if (propertyTypeCode == 'I') {
                        int valueInt = readSignedInt(inputStream, readBuffer);
                        recordHandler.propertyValueInt(valueInt);
                    } else if (propertyTypeCode == 'F') {
                        float valueFloat = readFloat(inputStream, readBuffer);
                        recordHandler.propertyValueFloat(valueFloat);
                    } else if (propertyTypeCode == 'D') {
                        double valueDouble = readDouble(inputStream, readBuffer);
                        recordHandler.propertyValueDouble(valueDouble);
                    } else if (propertyTypeCode == 'L') {
                        long valueLong = readLong(inputStream, readBuffer);
                        recordHandler.propertyValueLong(valueLong);
                    } else if (propertyTypeCode == 'S') {
                        int textLength = readUint32(inputStream, readBuffer);
                        String valueString = readString(inputStream, readBuffer, textLength);
                        recordHandler.propertyValueString(valueString);
                    } else if (propertyTypeCode == 'R') {
                        processRawBytes(recordHandler);
                    } else if (propertyTypeCode == 'f') {
                        processFloatArray(recordHandler);
                    } else if (propertyTypeCode == 'd') {
                        processDoubleArray(recordHandler);
                    } else if (propertyTypeCode == 'l') {
                        processLongArray(recordHandler);
                    } else if (propertyTypeCode == 'i') {
                        processIntArray(recordHandler);
                    } else if (propertyTypeCode == 'b') {
                        processBooleanArray(recordHandler);
                    } else {
                        throw new UnsupportedOperationException("Unable to recognize property type: " + propertyTypeCode);
                    }
                }
                while (endOffset != bytesRead) {
                    parseRecord(recordHandler);
                }
                recordHandler.endOfRecord();
            } else {
                doSkip(inputStream, endOffset - bytesRead);
            }
            recordDepth--;
        }
    }

    private void processBooleanArray(FbxRecordHandler recordHandler) throws IOException {
        int arrayLength = readUint32(inputStream, readBuffer);
        int encoding = readUint32(inputStream, readBuffer);
        int compressedLength = readUint32(inputStream, readBuffer);
        BooleanStream booleanStream = recordHandler.propertyValueBooleanArray(arrayLength);
        if (booleanStream != null) {
            if (encoding == 0) {
                for (int i = 0; i < arrayLength; i++) {
                    boolean value = readBoolean(inputStream, readBuffer);
                    booleanStream.write(value);
                }
            } else {
                byte[] deflatedBuffer = new byte[compressedLength];
                doFillBuffer(inputStream, deflatedBuffer, compressedLength);
                InflaterInputStream inflaterInputStream = new InflaterInputStream(new ByteArrayInputStream(deflatedBuffer));
                for (int i = 0; i < arrayLength; i++) {
                    boolean value = readBoolean(inflaterInputStream, readBuffer);
                    booleanStream.write(value);
                }
                inflaterInputStream.close();
            }
            booleanStream.close();
        } else {
            if (encoding == 0)
                doSkip(inputStream, arrayLength * 1);
            else
                doSkip(inputStream, compressedLength);
        }
    }

    private void processIntArray(FbxRecordHandler recordHandler) throws IOException {
        int arrayLength = readUint32(inputStream, readBuffer);
        int encoding = readUint32(inputStream, readBuffer);
        int compressedLength = readUint32(inputStream, readBuffer);
        IntStream intStream = recordHandler.propertyValueIntArray(arrayLength);
        if (intStream != null) {
            if (encoding == 0) {
                for (int i = 0; i < arrayLength; i++) {
                    int value = readSignedInt(inputStream, readBuffer);
                    intStream.write(value);
                }
            } else {
                byte[] deflatedBuffer = new byte[compressedLength];
                doFillBuffer(inputStream, deflatedBuffer, compressedLength);
                InflaterInputStream inflaterInputStream = new InflaterInputStream(new ByteArrayInputStream(deflatedBuffer));
                for (int i = 0; i < arrayLength; i++) {
                    int value = readSignedInt(inflaterInputStream, readBuffer);
                    intStream.write(value);
                }
                inflaterInputStream.close();
            }
            intStream.close();
        } else {
            if (encoding == 0)
                doSkip(inputStream, arrayLength * 4);
            else
                doSkip(inputStream, compressedLength);
        }
    }

    private void processLongArray(FbxRecordHandler recordHandler) throws IOException {
        int arrayLength = readUint32(inputStream, readBuffer);
        int encoding = readUint32(inputStream, readBuffer);
        int compressedLength = readUint32(inputStream, readBuffer);
        LongStream longStream = recordHandler.propertyValueLongArray(arrayLength);
        if (longStream != null) {
            if (encoding == 0) {
                for (int i = 0; i < arrayLength; i++) {
                    long value = readLong(inputStream, readBuffer);
                    longStream.write(value);
                }
            } else {
                byte[] deflatedBuffer = new byte[compressedLength];
                doFillBuffer(inputStream, deflatedBuffer, compressedLength);
                InflaterInputStream inflaterInputStream = new InflaterInputStream(new ByteArrayInputStream(deflatedBuffer));
                for (int i = 0; i < arrayLength; i++) {
                    long value = readLong(inflaterInputStream, readBuffer);
                    longStream.write(value);
                }
                inflaterInputStream.close();
            }
            longStream.close();
        } else {
            if (encoding == 0)
                doSkip(inputStream, arrayLength * 8);
            else
                doSkip(inputStream, compressedLength);
        }
    }

    private void processDoubleArray(FbxRecordHandler recordHandler) throws IOException {
        int arrayLength = readUint32(inputStream, readBuffer);
        int encoding = readUint32(inputStream, readBuffer);
        int compressedLength = readUint32(inputStream, readBuffer);
        DoubleStream doubleStream = recordHandler.propertyValueDoubleArray(arrayLength);
        if (doubleStream != null) {
            if (encoding == 0) {
                for (int i = 0; i < arrayLength; i++) {
                    double value = readDouble(inputStream, readBuffer);
                    doubleStream.write(value);
                }
            } else {
                byte[] deflatedBuffer = new byte[compressedLength];
                doFillBuffer(inputStream, deflatedBuffer, compressedLength);
                InflaterInputStream inflaterInputStream = new InflaterInputStream(new ByteArrayInputStream(deflatedBuffer));
                for (int i = 0; i < arrayLength; i++) {
                    double value = readDouble(inflaterInputStream, readBuffer);
                    doubleStream.write(value);
                }
                inflaterInputStream.close();
            }
            doubleStream.close();
        } else {
            if (encoding == 0)
                doSkip(inputStream, arrayLength * 8);
            else
                doSkip(inputStream, compressedLength);
        }
    }

    private void processFloatArray(FbxRecordHandler recordHandler) throws IOException {
        int arrayLength = readUint32(inputStream, readBuffer);
        int encoding = readUint32(inputStream, readBuffer);
        int compressedLength = readUint32(inputStream, readBuffer);
        FloatStream floatStream = recordHandler.propertyValueFloatArray(arrayLength);
        if (floatStream != null) {
            if (encoding == 0) {
                for (int i = 0; i < arrayLength; i++) {
                    float value = readFloat(inputStream, readBuffer);
                    floatStream.write(value);
                }
            } else {
                byte[] deflatedBuffer = new byte[compressedLength];
                doFillBuffer(inputStream, deflatedBuffer, compressedLength);
                InflaterInputStream inflaterInputStream = new InflaterInputStream(new ByteArrayInputStream(deflatedBuffer));
                for (int i = 0; i < arrayLength; i++) {
                    float value = readFloat(inflaterInputStream, readBuffer);
                    floatStream.write(value);
                }
                inflaterInputStream.close();
            }
            floatStream.close();
        } else {
            if (encoding == 0)
                doSkip(inputStream, arrayLength * 4);
            else
                doSkip(inputStream, compressedLength);
        }
    }

    private void processRawBytes(FbxRecordHandler recordHandler) throws IOException {
        int rawLength = readUint32(inputStream, readBuffer);
        OutputStream os = recordHandler.propertyValueRawBytes(rawLength);
        if (os != null) {
            int remainder = rawLength;
            while (remainder > 0) {
                int readCount = Math.min(remainder, readBuffer.length);
                doFillBuffer(inputStream, readBuffer, readCount);
                os.write(readBuffer, 0, readCount);
                remainder -= readCount;
            }
            os.close();
        } else {
            doSkip(inputStream, rawLength);
        }
    }

    private double readDouble(InputStream inputStream, byte[] buffer) throws IOException {
        return Double.longBitsToDouble(readLong(inputStream, buffer));
    }

    private long readLong(InputStream inputStream, byte[] buffer) throws IOException {
        doFillBuffer(inputStream, buffer, 8);
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result |= (buffer[i] & 0xffL) << (i * 8);
        }
        return result;
    }

    private float readFloat(InputStream inputStream, byte[] buffer) throws IOException {
        return Float.intBitsToFloat(readSignedInt(inputStream, buffer));
    }

    private int readSignedInt(InputStream inputStream, byte[] buffer) throws IOException {
        return readUint32(inputStream, buffer);
    }

    private boolean readBoolean(InputStream inputStream, byte[] buffer) throws IOException {
        doFillBuffer(inputStream, buffer, 1);
        return (buffer[0] & 0x01) > 0;
    }

    private short readShort(InputStream inputStream, byte[] buffer) throws IOException {
        doFillBuffer(inputStream, buffer, 2);
        short result = 0;
        for (int i = 0; i < 2; i++) {
            result |= (buffer[i] & 0xff) << (i * 8);
        }
        return result;
    }

    private char readChar(InputStream inputStream, byte[] buffer) throws IOException {
        doFillBuffer(inputStream, buffer, 1);
        return (char) buffer[0];
    }

    private String readString(InputStream inputStream, byte[] buffer, int length) throws IOException {
        doFillBuffer(inputStream, buffer, length);
        return new String(buffer, 0, length, StandardCharsets.US_ASCII);
    }

    private int readUint8t(InputStream inputStream, byte[] buffer) throws IOException {
        doFillBuffer(inputStream, buffer, 1);
        return buffer[0];
    }

    private int readUint32(InputStream inputStream, byte[] buffer) throws IOException {
        int byteCount = 4;
        doFillBuffer(inputStream, buffer, byteCount);
        int result = 0;
        for (int i = 0; i < byteCount; i++) {
            result |= (buffer[i] & 0xff) << (i * 8);
        }
        return result;
    }

    private void doSkip(InputStream inputStream, int length) throws IOException {
        int remainder = length;
        while (remainder > 0) {
            long skipped = inputStream.skip(remainder);
            remainder -= skipped;
        }
        if (this.inputStream == inputStream) {
            bytesRead += length;
        }
    }

    private void doFillBuffer(InputStream inputStream, byte[] buffer, int length) throws IOException {
        int offset = 0;
        int remainder = length;
        while (remainder > 0) {
            int readCount = inputStream.read(buffer, offset, remainder);
            if (readCount == -1)
                throw new EOFException("Reached the end of file, when expected to fill buffer");

            remainder -= readCount;
            offset += readCount;
        }
        if (this.inputStream == inputStream) {
            bytesRead += length;
        }
    }

    public static void main(String[] args) throws Exception {
        //File file = new File("C:\\Users\\User\\Downloads\\uploads_files_186032_Maskboy.FBX");
        File file = new File("C:\\Users\\User\\Downloads\\uploads_files_2118289_Legendary_Robot.fbx");
        FbxParser parser = new FbxParser(1024);

        ModelRecordHandler modelRecordHandler = new ModelRecordHandler();
        NodeAttributeRecordHandler nodeAttributeRecordHandler = new NodeAttributeRecordHandler();
        GeometryRecordHandler geometryRecordHandler = new GeometryRecordHandler();
        ConnectionsRecordHandler connectionsRecordHandler = new ConnectionsRecordHandler();
        TextureRecordHandler textureRecordHandler = new TextureRecordHandler();
        MaterialRecordHandler materialRecordHandler = new MaterialRecordHandler();
        AnimationStackRecordHandler animationStackRecordHandler = new AnimationStackRecordHandler();
        AnimationLayerRecordHandler animationLayerRecordHandler = new AnimationLayerRecordHandler();

        CompositeFbxHandler handler = new CompositeFbxHandler();

        CompositeFbxRecordHandler objectsHandler = new CompositeFbxRecordHandler("Objects");
        objectsHandler.setRecordHandler("Geometry", geometryRecordHandler);
        objectsHandler.setRecordHandler("NodeAttribute", nodeAttributeRecordHandler);
        objectsHandler.setRecordHandler("Model", modelRecordHandler);
        objectsHandler.setRecordHandler("Texture", textureRecordHandler);
        objectsHandler.setRecordHandler("Material", materialRecordHandler);
        objectsHandler.setRecordHandler("AnimationStack", animationStackRecordHandler);
        objectsHandler.setRecordHandler("AnimationLayer", animationLayerRecordHandler);

        handler.setRecordHandler("Objects", objectsHandler);
        handler.setRecordHandler("Connections", connectionsRecordHandler);

        try (FileInputStream fis = new FileInputStream(file)) {
            parser.parse(fis, handler, (int) file.length());
        }

        System.out.println("Models: "+modelRecordHandler.getModels().size);
        System.out.println("Geometries: "+geometryRecordHandler.getGeometries().size);
        System.out.println("Connections: "+connectionsRecordHandler.getConnections().size);
        System.out.println("Node attributes: "+nodeAttributeRecordHandler.getNodeAttributes().size);
    }
}
