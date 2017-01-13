package com.gordolio.budgie.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private static final List<String> FILES = new ArrayList<>();
    static {
        try {
            FILES.addAll(getResourceFiles("/sounds/")
                    .stream()
                    .map(v->"/sounds/"+v)
                    .collect(Collectors.toList()));
        }catch(Exception ex) {
            LOG.error("could not load files", ex);
        }
    }

    public void playSound() {
        String file = FILES.get(RandomUtils.nextInt(0, FILES.size()));
        this.playWav(file);
    }

    public void playWav(String file) {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                        SoundService.class.getResourceAsStream(file));
                clip.open(inputStream);
                clip.start();
            } catch (Exception e) {
                LOG.error("Error playing sound", e);
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
