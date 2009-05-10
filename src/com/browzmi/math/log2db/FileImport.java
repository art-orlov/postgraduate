package com.browzmi.math.log2db;

import com.browzmi.math.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.*;

public abstract class FileImport {
    protected final EntityManager em;
    protected EntityTransaction et;

    protected FileImport() {
        this.em = Configuration.getInstance().em();
    }

    protected final void doImport(String fileName) throws IOException {
        try {
            et = em.getTransaction();

            et.begin();

            processFile(fileName);

            et.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (et != null) {
                et.rollback();
            }
        } finally {
            em.close();
        }
    }

    private void processFile(String fileName) throws IOException {
        final long start = System.currentTimeMillis();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
        String line;
        int cnt = -1;
        while ((line = reader.readLine()) != null) {
            final String label = processLine(line);

            cnt = (cnt + 1) % 1000;
            if (cnt == 0) {
                System.out.print('\n' + label + ':');
            } else if (cnt % 10 == 0) {
                System.out.print('.');
            }

            if (cnt % 500 == 0) {
                et.commit();
                et.begin();
            }
        }
        reader.close();
        System.out.println(String.format("Import completed in %d seconds", (System.currentTimeMillis() - start) / 1000));
    }

    protected abstract String processLine(String line);
}
