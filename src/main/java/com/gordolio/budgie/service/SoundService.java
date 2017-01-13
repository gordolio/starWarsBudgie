package com.gordolio.budgie.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class SoundService {

    private static final Logger LOG = LoggerFactory.getLogger(SoundService.class);

    public static final List<String> FILE_NAMES = new ArrayList<>();
    private static final Map<String,byte[]> SOUNDS = new HashMap<>();
    static {
        try {
            getResourceFiles("/sounds/")
                    .stream()
                    .map(v->v.replace(".wav",""))
                    .forEach(f->{
                        try {
                            FILE_NAMES.add(f);
                            ByteArrayOutputStream output = new ByteArrayOutputStream();
                            InputStream inputStream = SoundService.class.getResourceAsStream("/sounds/" + f + ".wav");
                            byte[] buffer = new byte[2048];
                            int bytesRead;
                            while (-1 != (bytesRead = inputStream.read(buffer))) {
                                output.write(buffer, 0, bytesRead);
                            }
                            SOUNDS.put(f, output.toByteArray());
                            inputStream.close();
                        } catch (Exception ex) {
                            LOG.error("Could not read sound files", ex);
                        }
                    });
        }catch(Exception ex) {
            LOG.error("could not load files", ex);
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
                ByteArrayInputStream audioBytes = new ByteArrayInputStream(SOUNDS.get(file));
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(audioBytes);
                clip.open(inputStream);
                clip.start();
                inputStream.close();
            } catch (Exception e) {
                LOG.error("Error playing sound: " + file, e);
            }
        }).start();
    }

    private static List<String> getResourceFiles(String path) throws IOException {
        List<String> filenames = new ArrayList<>();

        try(InputStream in = getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;
            while((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        }
        return filenames;
    }

    private static InputStream getResourceAsStream(String resource ) {
        final InputStream in = getContextClassLoader().getResourceAsStream(resource);
        return in == null ? SoundService.class.getResourceAsStream(resource) : in;
    }

    private static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}
