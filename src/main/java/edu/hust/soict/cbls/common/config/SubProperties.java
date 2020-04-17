package edu.hust.soict.cbls.common.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SubProperties extends Properties {

    private String prefix;

    public SubProperties(String prefix, Map<Object, Object> runtimeProps){
        this.prefix = prefix;
        this.runtimeProps = runtimeProps;
    }

    @Override
    public String getProperty(String key){
        try{
            String value = super.getProperty(keyWithPrefix(key));
            if(value == null)
                return super.getProperty(key);
            else
                return value;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
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
            String value = getProperty(keyWithPrefix(key));
            if(value == null)
                return Integer.parseInt(getProperty(key));
            else
                return Integer.parseInt(value);

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
            String value = getProperty(keyWithPrefix(key));
            if(value == null)
                return Long.parseLong(getProperty(key));
            else
                return Long.parseLong(value);
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
            String value = getProperty(keyWithPrefix(key));
            if(value == null)
                return Double.parseDouble(getProperty(key));
            else
                return Double.parseDouble(value);
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
            String value = getProperty(keyWithPrefix(key));
            if(value == null)
                return Boolean.parseBoolean(getProperty(key));
            else
                return Boolean.parseBoolean(value);
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
            String value = getProperty(keyWithPrefix(key));
            if(value == null)
                return Arrays.asList(getProperty(key).split(","));
            else
                return Arrays.asList(value.split(","));
        } catch (Exception ignored) {
            return Collections.emptyList();
        }
    }

    private String keyWithPrefix(String key){
        return prefix + "." + key;
    }

}
