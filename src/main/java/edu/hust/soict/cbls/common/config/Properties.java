package edu.hust.soict.cbls.common.config;

import edu.hust.soict.cbls.common.utils.StringUtils;

import java.io.IOException;
import java.util.*;

public class Properties extends java.util.Properties{
    private static final String DEFAULT_NAME = "config.properties";
    protected Map<Object, Object> runtimeProps;

    public Properties() {
        try {
            this.runtimeProps = new HashMap<>();
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SubProperties toSubProperties(String prefix){
        return new SubProperties(prefix, this.runtimeProps);
    }

    /**
     * Lấy về giá trị có kiểu int
     *
     * @param key    key của giá trị cần lấy
     * @param defVal giá trị mặc định trả về
     * @return số int của giá trị cần lấy hoặc giá trị mặc định nếu
     * không tìm thấy giá trị nào ứng với key cần tìm
     */
    public int getIntProperty(String key, int defVal) {
        try {
            return Integer.parseInt(getProperty(key));
        } catch (Exception ignored) {
            return defVal;
        }
    }

    /**
     * Lấy về giá trị có kiểu long
     *
     * @param key    key của giá trị cần lấy
     * @param defVal giá trị mặc định trả về
     * @return số long của giá trị cần lấy hoặc giá trị mặc định nếu
     * không tìm thấy giá trị nào ứng với key cần tìm
     */
    public long getLongProperty(String key, long defVal) {
        try {
            return Long.parseLong(getProperty(key));
        } catch (Exception ignored) {
            return defVal;
        }
    }

    /**
     * Lấy về giá trị có kiểu double
     *
     * @param key    key của giá trị cần lấy
     * @param defVal giá trị mặc định trả về
     * @return số double của giá trị cần lấy hoặc giá trị mặc định nếu
     * không tìm thấy giá trị nào ứng với key cần tìm
     */
    public double getDoubleProperty(String key, double defVal) {
        try {
            return Double.parseDouble(getProperty(key));
        } catch (Exception ignored) {
            return defVal;
        }
    }

    /**
     * Lấy về giá trị có kiểu bool
     *
     * @param key    key của giá trị cần lấy
     * @param defVal giá trị mặc định trả về
     * @return số double của giá trị cần lấy hoặc giá trị mặc định nếu
     * không tìm thấy giá trị nào ứng với key cần tìm
     */
    public boolean getBoolProperty(String key, boolean defVal) {
        try {
            return Boolean.parseBoolean(getProperty(key));
        } catch (Exception ignored) {
            return defVal;
        }
    }

    /**
     * Lấy về giá trị có kiểu list String
     *
     * @param key key của giá trị cần lấy
     * @return list String các giá trị được phân cách bởi dấu phẩy
     */
    public List<String> getCollection(String key) {
        try {
            String val = getProperty(key);
            if(StringUtils.isNullOrEmpty(val)){
                return Collections.emptyList();
            } else{
                return Arrays.asList(val.split(","));
            }
        } catch (Exception ignored) {
            return Collections.emptyList();
        }
    }

    private void load() throws IOException {
        load(Properties.class.getClassLoader().getResourceAsStream(DEFAULT_NAME));
    }
}
