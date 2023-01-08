package com.gempukku.libgdx.lib.artemis.audio;


import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

public class AudioSystem extends BaseSystem {
    private float masterVolume = 0.05f;
    private float musicVolume = 1f;
    private float fxVolume = 1f;

    private ObjectMap<String, ObjectMap<String, Sound>> audioBanks = new ObjectMap<>();

    private long musicFadeOutTime = 1000;

    private long musicSwitchTime;
    private Music oldBackgroundMusic;
    private Music backgroundMusic;

    @Override
    protected void initialize() {
        world.getAspectSubscriptionManager().get(Aspect.all(AudioBankComponent.class))
                .addSubscriptionListener(
                        new EntitySubscription.SubscriptionListener() {
                            @Override
                            public void inserted(IntBag entities) {
                                for (int i = 0; i < entities.size(); i++) {
                                    audioBankAdded(world.getEntity(entities.get(i)));
                                }
                            }

                            @Override
                            public void removed(IntBag entities) {
                                for (int i = 0; i < entities.size(); i++) {
                                    audioBankRemoved(world.getEntity(entities.get(i)));
                                }
                            }
                        }
                );
    }

    private void audioBankAdded(Entity audioBankEntity) {
        AudioBankComponent audioBank = audioBankEntity.getComponent(AudioBankComponent.class);

        ObjectMap<String, Sound> sounds = new ObjectMap<>();

        for (SoundElement soundElement : audioBank.getSounds()) {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(soundElement.getPath()));
            sounds.put(soundElement.getName(), sound);
        }

        audioBanks.put(audioBank.getName(), sounds);
    }

    private void audioBankRemoved(Entity audioBankEntity) {
        AudioBankComponent audioBank = audioBankEntity.getComponent(AudioBankComponent.class);
        ObjectMap<String, Sound> sounds = audioBanks.remove(audioBank.getName());
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
    }

    public void setMasterVolume(float volume) {
        masterVolume = volume;
    }

    public void setMusicVolume(float volume) {
        musicVolume = volume;
    }

    public void setFXVolume(float volume) {
        fxVolume = volume;
    }

    public void playSound(String audioBank, String sound) {
        ObjectMap<String, Sound> sounds = audioBanks.get(audioBank);
        sounds.get(sound).play(fxVolume * masterVolume);
    }

    public void setBackgroundMusic(Music music, long fadeTime) {
        if (backgroundMusic != music) {
            musicFadeOutTime = fadeTime;
            if (oldBackgroundMusic != null)
                oldBackgroundMusic.stop();
            oldBackgroundMusic = backgroundMusic;
            backgroundMusic = music;
            if (backgroundMusic != null) {
                backgroundMusic.setVolume(0);
                backgroundMusic.play();
            }
            musicSwitchTime = System.currentTimeMillis();
        }
    }

    public boolean isBackgroundMusic(Music music) {
        return backgroundMusic == music;
    }

    @Override
    protected void processSystem() {
        long currentTime = System.currentTimeMillis();
        if (currentTime < musicSwitchTime + musicFadeOutTime) {
            float progress = 1f * (currentTime - musicSwitchTime) / musicFadeOutTime;
            if (oldBackgroundMusic != null)
                oldBackgroundMusic.setVolume(musicVolume * masterVolume * (1 - progress));
            if (backgroundMusic != null)
                backgroundMusic.setVolume(musicVolume * masterVolume * progress);
        } else {
            if (oldBackgroundMusic != null)
                oldBackgroundMusic.stop();
            oldBackgroundMusic = null;
            if (backgroundMusic != null)
                backgroundMusic.setVolume(musicVolume * masterVolume);
        }
    }

    @Override
    protected void dispose() {
        for (ObjectMap<String, Sound> audioBank : audioBanks.values()) {
            for (Sound sound : audioBank.values()) {
                sound.dispose();
            }
        }
        audioBanks.clear();
    }
}
