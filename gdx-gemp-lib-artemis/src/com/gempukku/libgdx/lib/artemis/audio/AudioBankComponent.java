package com.gempukku.libgdx.lib.artemis.audio;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

public class AudioBankComponent extends Component {
    private String name;
    private Array<SoundElement> sounds = new Array<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Array<SoundElement> getSounds() {
        return sounds;
    }

    public void setSounds(Array<SoundElement> sounds) {
        this.sounds = sounds;
    }
}
