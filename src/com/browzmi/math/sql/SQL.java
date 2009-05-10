package com.browzmi.math.sql;

import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 16.12.2007
 * Time: 15:13:30
 */
public final class SQL {
    public static final String COMPUTE_OPERATION = readScript("clique/compute_operation.sql");
    public static final String COMPUTE_OPERATION_BY_HOST = readScript("clique/compute_operation_by_host.sql");
    public static final String COMPUTE_RELATION = readScript("clique/compute_relation.sql");
    public static final String COMPUTE_RELATION_BY_UNIQUE_URLS = readScript("clique/compute_relation_by_unique_urls.sql");
    public static final String CREATE_FN = readScript("clique/create_fn.sql");
    public static final String DELETE_ALL = readScript("clique/delete_all.sql");
    public static final String DROP_FN = readScript("clique/drop_fn.sql");
	public static final String SELECT_URLS_BY_CENTER = readScript("clique/select_urls_by_center.sql");
	public static final String CREATE_VIEW = readScript("clique/create_view.sql");
	public static final String SELECT_HOST = readScript("select_host.sql");
	public static final String SELECT_TAG = readScript("select_tag.sql");
	public static final String SELECT_URL = readScript("select_url.sql");

	private static String readScript(String name) {
        final InputStream is = SQL.class.getResourceAsStream(name);
        if (is == null) {
			//noinspection ThrowableInstanceNeverThrown
			throw new RuntimeException(new FileNotFoundException(name));
        }
        try {
            final int total = is.available(); 
            final byte[] bytes = new byte[total];

            if (is.read(bytes, 0, is.available()) != total) {
                throw new IOException("Error reading file");
            }

            return new String(bytes, "utf-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
