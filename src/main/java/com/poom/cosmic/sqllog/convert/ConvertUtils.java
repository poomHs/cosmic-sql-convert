package com.poom.cosmic.sqllog.convert;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;


/**
 * @author kun.jiang@hand-china.com 2021-07-07 20:01
 */
public class ConvertUtils {


    public static boolean isSql(String text) {
        if (text == null || text.length() <= 0) {
            return false;
        }

//        if (strNum(text, ": ") > 1) {
//            return false;
//        }

        if (!text.contains("?")) {
            return false;
        }

        String tempText = fixSql(text).toUpperCase(Locale.ROOT);
        boolean select = tempText.contains("SELECT") && tempText.contains("FROM");
        boolean insert = tempText.contains("INSERT INTO") && tempText.contains("VALUES");
        boolean delete = tempText.contains("DELETE") && tempText.contains("FROM");
        boolean update = tempText.contains("UPDATE") && tempText.contains("SET");

        return select || insert || delete || update;

    }


    public static boolean isParam(String text) {
        if (text == null || text.length() <= 0) {
            return false;
        }

//        if (strNum(text, ": ") > 1) {
//            return false;
//        }

        final String tempText = fixParam(text);

        String[] split = tempText.split(",");
        if(split.length<1){
            return  false;
        }
//        for (String s : split) {
//            s = s.trim();
//            if (!"null".equals(s)) {
//                if (!(s.contains("(") && s.contains(")"))) {
//                    return false;
//                }
////                String type = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
////                if (!FieldType.isNormalType(type)) {
////                    return false;
////                }
//            }
//        }

        return true;

    }

    public static int strNum(String text, String checkFlag) {
        String var = text;
        int res = 0;
        while (var.contains(checkFlag)) {
            var = var.substring(var.indexOf(checkFlag) + checkFlag.length()).trim();
            res++;
        }
        return res;
    }

    @NotNull
    public static String fixSql(String text) {
        String paramFlag = ": ";
        if (text.contains(paramFlag)) {
            return text.substring(text.lastIndexOf(paramFlag) + 2).trim();
        } else {
            return text.trim();
        }
    }

    @NotNull
    public static String fixParam(String text) {
        String paramFlag = ": ";
        if (text.contains(paramFlag)) {
            return text.substring(text.lastIndexOf(paramFlag) + 2).trim();
        } else {
            return text.trim();
        }
    }


    public static String convert(String sql, String params) {
        if (!isSql(sql) || !isParam(params)) {
            return null;
        }
        //参数个数不匹配
        if(sql.split("\\?").length!=(params.split(",").length+1)){
            return null;
        }

        sql = fixSql(sql);
        if (!sql.contains("?")) {
            return null;
        }

        String[] split = fixParam(params).split(",");
        for (String s : split) {
//            String trim = s.trim();
//            if ("null".equals(trim)|| StringUtil.isNotEmpty(trim)) {
//                int index = sql.indexOf("?");
//                int end = index;
//                char c;
//                int isNullFalg = 0;
//                while (true) {
//                    index--;
//                    c = sql.charAt(index);
//                    if (c != ' ') {
//                        if (c == '=') {
//                            char preChar = sql.charAt(index - 1);
//                            if (preChar == '!') {
//                                // is not null
//                                isNullFalg = 1;
//                                index--;
//                            } else if (preChar == ':' || preChar == '<' || preChar == '>') {
//                                // 正常赋值
//                                isNullFalg = 0;
//                            } else {
//                                // is null
//                                isNullFalg = 2;
//                            }
//                        }
//                        break;
//                    }
////                }
//                int isNullFalg = 0;
//                if(StringUtil.isEmpty(s)){
//                    int index = sql.indexOf("?");
//                     int end = index;
//                switch (isNullFalg) {
//                    case 1:
//                        // 这个?不转义，这方法还识别不了，这个有点坑
//                        sql = sql.replaceFirst((sql.substring(index, end) + "\\?"), " is not null ");
//                        break;
//                    case 2:
//                        sql = sql.replaceFirst((sql.substring(index, end) + "\\?"), " is null ");
//                        break;
//                    case 0:
//                    default:
//                        sql = sql.replaceFirst("\\?", trim);
//                }
//
//            } else {
//                String param = trim.substring(0, trim.indexOf("("));
//                String type = trim.substring(trim.indexOf("(") + 1, trim.indexOf(")"));
//                String defaultStr = FieldType.getDefaultValue(type);
//                String finalParam = defaultStr + param + defaultStr;
//                sql = sql.replaceFirst("\\?", finalParam);
//            }
            String finalParam = s;
//            if(s!=null&&s.length()>0){
//                finalParam = !s.matches("\\d+")?String.format("%s"+s+"%s","'","'"):s;
//            }
            sql = sql.replaceFirst("\\?", finalParam);

            if (!sql.contains("?")) {
                break;
            }

        }

        return sql;

    }


    public static void main(String[] args) {
        String sql = " SELECT TOP 11,0 A.FId \"id\", A.fnumber \"number\", CASE WHEN B.fname IS NULL THEN A.fname WHEN B.fname = '' THEN A.fname WHEN B.fname = ' ' THEN A.fname ELSE B.fname END \"name\"\n" +
                "FROM T_fa_assetcategory A \n" +
                "LEFT JOIN T_fa_assetcategory_L B ON B.FId=A.FId AND B.FLocaleId='zh_CN'\n" +
                "WHERE A.fisleaf = ? AND (A.fctrlstrategy = ? AND A.fstatus = ? OR A.FId in ( ( select fdataid from T_fa_assetcategory_U where fuseorgid = 100000 ))) AND A.fstatus = ? AND A.fenable = ? AND (A.fnumber like ?  OR B.fname like ?)\n" +
                "ORDER BY A.fnumber";
        String params = "'1','5','C','C','1','%SF%','%SF%'";
        String convert = convert(sql, params);
        System.out.println("convert = " + convert);
    }

}
