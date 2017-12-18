package nc.opt.mobile.optmobile.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 2761oli on 23/10/2017.
 */

public class DateConverter {
    private static final String TAG = DateConverter.class.getName();

    private static final String PATTERN_DTO = "dd/MM/yyyy HH:mm:ss";
    private static final String PATTERN_ENTITY = "yyyyMMddHHmmss";
    private static final String PATTERN_UI = "dd MMM yyyy à HH:mm";
    private static final String PATTERN_AFTER_HIP = "yyyy-MM-dd'T'HH:mm:ss";

    private static final SimpleDateFormat simpleDtoDateFormat = new SimpleDateFormat(PATTERN_DTO, Locale.FRANCE);
    private static final SimpleDateFormat simpleUiDateFormat = new SimpleDateFormat(PATTERN_UI, Locale.FRANCE);
    private static final SimpleDateFormat simpleEntityDateFormat = new SimpleDateFormat(PATTERN_ENTITY, Locale.FRANCE);
    private static final SimpleDateFormat simpleAfterShipDateFormat = new SimpleDateFormat(PATTERN_AFTER_HIP);

    private DateConverter() {
    }

    /**
     * Transformation d'une date de type yyyyMMddHHmmss vers le format dd MMM yy HH:mm
     *
     * @param dateEntity
     * @return
     */
    public static String convertDateEntityToUi(Long dateEntity) {
        if (dateEntity != null) {
            try {
                return simpleUiDateFormat.format(simpleEntityDateFormat.parse(String.valueOf(dateEntity)));
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return null;
    }

    public static String howLongFromNow(Long dateEntity) {
        if (dateEntity != null) {
            return howLongFromNow(DateConverter.convertDateEntityToDto(dateEntity));
        }
        return null;
    }

    private static String howLongFromNow(String dateDto) {
        if (dateDto == null) {
            return null;
        }
        try {
            Date now = Calendar.getInstance().getTime();
            Date date = simpleDtoDateFormat.parse(dateDto);
            long duration = now.getTime() - date.getTime();

            long nbSecond = duration / 1000;
            long nbMinute = nbSecond / 60;
            long nbHour = nbMinute / 60;
            long nbDay = nbHour / 24;
            long nbWeek = nbDay / 7;
            long nbMonth = nbDay / 30;
            long nbYear = nbMonth / 12;

            // On est au dessus de l'année on affiche les années
            if (nbYear >= 1) {
                return String.valueOf(nbYear).concat(" années");
            }
            // On est en dessous de l'année on affiche les mois
            if (nbMonth >= 1) {
                return String.valueOf(nbMonth).concat(" mois");
            }
            // On est en dessous du mois on affiche les semaines
            if (nbWeek >= 1) {
                return String.valueOf(nbWeek).concat(" sem");
            }
            // On est en dessous de la semaine on affiche les jours
            if (nbDay >= 1) {
                return String.valueOf(nbDay).concat(" j");
            }
            // On est en dessous de la journée on affiche les heures
            if (nbHour >= 1) {
                return String.valueOf(nbHour).concat(" hr");
            }
            // On est en dessous de l'heure on affiche les minutes
            if (nbMinute >= 1) {
                return String.valueOf(nbMinute).concat(" min");
            }
            // On est inférieur à la minute, on affiche les secondes
            return String.valueOf(nbSecond).concat(" sec");
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    /**
     * Transformation d'une date de type dd/MM/yyyy HH:mm:ss vers le format yyyyMMddHHmmss en Long
     *
     * @param dateDto
     * @return Long
     */
    public static Long convertDateDtoToEntity(String dateDto) {
        try {
            String dateConverted = simpleEntityDateFormat.format(simpleDtoDateFormat.parse(dateDto));
            return Long.parseLong(dateConverted);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (NullPointerException e1) {
            Log.e(TAG, e1.getMessage(), e1);
        }
        return 0L;
    }

    /**
     * Transformation d'une date de type yyyy-MM-ddTHH:mm:ss vers le format yyyyMMddHHmmss en Long
     *
     * @param dateAferShip
     * @return
     */
    public static Long convertDateAfterShipToEntity(String dateAferShip) {
        try {
            String dateConverted = simpleEntityDateFormat.format(simpleAfterShipDateFormat.parse(dateAferShip));
            return Long.parseLong(dateConverted);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (NullPointerException e1) {
            Log.e(TAG, e1.getMessage(), e1);
        }
        return 0L;
    }

    /**
     * Transformation d'une date de type yyyyMMddHHmmss vers le format dd/MM/yyyy HH:mm:ss en Long
     *
     * @param dateEntity
     * @return String
     */
    public static String convertDateEntityToDto(Long dateEntity) {
        try {
            Date dateConverted = simpleEntityDateFormat.parse(String.valueOf(dateEntity));
            return simpleDtoDateFormat.format(dateConverted);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    /**
     * Va renvoyer la date du jour au format yyyyMMddHHmmss en Long
     *
     * @return Long
     */
    public static Long getNowEntity() {
        Calendar cal = Calendar.getInstance();
        return Long.parseLong(simpleEntityDateFormat.format(cal.getTime()));
    }

    /**
     * Va renvoyer la date du jour au format dd/MM/yyyy HH:mm:ss en String
     *
     * @return String
     */
    public static String getNowDto() {
        Calendar cal = Calendar.getInstance();
        return simpleDtoDateFormat.format(cal.getTime());
    }
}
