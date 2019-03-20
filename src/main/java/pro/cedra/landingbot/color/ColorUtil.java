package pro.cedra.landingbot.color;

import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;

import java.awt.*;
import java.util.List;

@UtilityClass
public class ColorUtil {

    public String getMostReadableColor (String baseColor) {
        return getMostReadableColor(getColorFromHex(baseColor), Lists.newArrayList(
            getColorFromHex("fff"), getColorFromHex("000"))).toString();
    }
    public Color getMostReadableColor (Color baseColor, List<Color> colors) {
        return colors.stream().sorted((color1, color2) -> (int)(getReadability(baseColor, color2) - getReadability(baseColor, color1)))
            .findFirst().get();
    }

    public double getReadability(Color color1, Color color2) {
        double c1Lum = getLuminance(color1);
        double c2Lum = getLuminance(color2);
        return (Math.max(c1Lum, c2Lum)+0.05) / (Math.min(c1Lum,c2Lum)+0.05);
    }

    public double getLuminance(Color color) {
        double RsRGB = color.getRed() / 255;
        double GsRGB = color.getGreen() / 255;
        double BsRGB = color.getBlue() / 255;
        double R,G,B;
        if (RsRGB <= 0.03928) {R = RsRGB / 12.92;} else {R = Math.pow(((RsRGB + 0.055) / 1.055), 2.4);}
        if (GsRGB <= 0.03928) {G = GsRGB / 12.92;} else {G = Math.pow(((GsRGB + 0.055) / 1.055), 2.4);}
        if (BsRGB <= 0.03928) {B = BsRGB / 12.92;} else {B = Math.pow(((BsRGB + 0.055) / 1.055), 2.4);}
        return (0.2126 * R) + (0.7152 * G) + (0.0722 * B);

    }

    public Color getColorFromHex(String color) {
        StringBuilder sb = new StringBuilder();
        if (color.length() == 3) {
            for (char ch: color.toCharArray()) {
                sb.append(ch).append(ch);
            }
            color = sb.toString();
        }
        return Color.decode("#" + color);
    }

    public static void main(String[] args) {
        System.out.println(getMostReadableColor("23be35"));
        System.out.println(getMostReadableColor("000"));
        System.out.println(getMostReadableColor("fff"));
    }
}
