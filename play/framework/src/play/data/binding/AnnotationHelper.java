package play.data.binding;

import java.lang.annotation.Annotation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.i18n.Lang;
import play.libs.I18N;

public class AnnotationHelper {

    /**
     * It can be something like As(lang={"fr,de","*"}, value={"dd-MM-yyyy","MM-dd-yyyy"})
     *
     * @param annotations
     * @param value
     * @return null if it cannot be converted because there is no annotation.
     * @throws ParseException
     *
     */
    public static Date getDateAs(Annotation[] annotations, String value) throws ParseException {
        // Look up for the BindAs annotation
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(As.class)) {
                    As as = (As) annotation;

                    String format = as.value()[0];
                    if (!StringUtils.isEmpty(format)) {
                        // This can be comma separated
                        Tuple tuple = getLocale(as.lang());
                        if (tuple != null) {
                            // Avoid NPE and get the last value if not specified
                            format = as.value()[tuple.index < as.value().length ? tuple.index : as.value().length - 1];
                            SimpleDateFormat sdf = new SimpleDateFormat(format, tuple.locale);
                            sdf.setLenient(false);
                        }
                    }
                    if (StringUtils.isEmpty(format)) {
                        format = I18N.getDateFormat();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat(format, Lang.getLocale());
                    sdf.setLenient(false);
                    return sdf.parse(value);

                }
            }
        }
        return null;
    }

    public static Tuple getLocale(String[] langs) {
        int i = 0;
        for (String l : langs) {
            String[] commaSeparatedLang = l.split(",");
            for (String lang : commaSeparatedLang) {
                if (Lang.get().equals(lang) || "*".equals(lang)) {
                    if ("*".equals(lang)) {
                        lang = Locale.getDefault().getLanguage();
                    }
                    Locale locale = Lang.getLocale(lang);
                    if (locale != null) {
                        return new Tuple(i, locale);
                    }
                }
            }
            i++;
        }
        return null;
    }

    /**
     * Contains the index of the locale inside the @As
     */
    private static class Tuple {
        
        public int index = -1;
        public Locale locale;

        public Tuple(int index, Locale locale) {
            this.locale = locale;
            this.index = index;
        }
    }
}
