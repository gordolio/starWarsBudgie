package com.gordolio.budgie.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


@Service
public class SoundService implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(SoundService.class);

    public static final List<String> FILE_NAMES = new ArrayList<>();
    private static final Map<String,byte[]> SOUNDS = new HashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    public void loadResources() {
        try {
            Resource[] resources = this.applicationContext.getResources("classpath*:/sounds/*.wav");
            for(Resource resource : resources) {
                String f = resource.getFilename().replace(".wav","");
                FILE_NAMES.add(f);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                InputStream inputStream = resource.getInputStream();
                byte[] buffer = new byte[2048];
                int bytesRead;
                while (-1 != (bytesRead = inputStream.read(buffer))) {
                    output.write(buffer, 0, bytesRead);
                }
                SOUNDS.put(f, output.toByteArray());
                inputStream.close();
            }
        } catch (IOException ex) {
            LOG.error("Error reading resources", ex);
        }
    }

    public void playAnySound() {
        String file = FILE_NAMES.get(RandomUtils.nextInt(0, FILE_NAMES.size()));
        this.playWav(file);
    }

    public void playWav(String file) {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                clip.addLineListener(event -> {
                    if(LineEvent.Type.STOP.equals(event.getType())) {
                        clip.close();
                    }
                });
                ByteArrayInputStream audioBytes = new ByteArrayInputStream(SOUNDS.get(file));
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(audioBytes);
                clip.open(inputStream);
                clip.start();
            } catch (Exception e) {
                LOG.error("Error playing sound: " + file, e);
            }
        }).start();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.loadResources();
    }
}
