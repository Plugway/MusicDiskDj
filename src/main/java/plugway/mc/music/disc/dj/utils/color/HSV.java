package plugway.mc.music.disc.dj.utils.color;

import static org.apache.commons.lang3.math.NumberUtils.min;

//by EasyRGB
public class HSV {
    private final double hue;
    private final double saturation;
    private final double value;

    public HSV(double hue, double saturation, double value){
        if ( hue < 0 ) hue += 1;
        if ( hue > 1 ) hue -= 1;
        this.hue = hue;
        this.saturation = saturation;
        this.value = value;
    }
    public HSV(int r, int g, int b){
        //R, G and B input range = 0 ÷ 255
        //H, S and V output range = 0 ÷ 1.0

        double R = ( r / 255.0 );
        double G = ( g / 255.0 );
        double B = ( b / 255.0 );

        double min = Math.min(Math.min( R, G), B );    //Min. value of RGB
        double max = Math.max(Math.max( R, G), B );    //Max. value of RGB
        double deltaMax = max - min;             //Delta RGB value

        double H = 0;
        double S;
        double V = max;
        
        if ( deltaMax == 0 )                     //This is a gray, no chroma...
        {
            H = 0;
            S = 0;
        }
        else                                    //Chromatic data...
        {
            S = deltaMax / max;

            double deltaR = ( ( ( max - R ) / 6 ) + ( deltaMax / 2 ) ) / deltaMax;
            double deltaG = ( ( ( max - G ) / 6 ) + ( deltaMax / 2 ) ) / deltaMax;
            double deltaB = ( ( ( max - B ) / 6 ) + ( deltaMax / 2 ) ) / deltaMax;

            if      ( R == max ) H = deltaB - deltaG;
            else if ( G == max ) H = ( 1 / 3.0 ) + deltaR - deltaB;
            else if ( B == max ) H = ( 2 / 3.0 ) + deltaG - deltaR;

            if ( H < 0 ) H += 1;
            if ( H > 1 ) H -= 1;
        }
        if (H < 0)
            H = 0;
        if (H > 1)
            H = 1;
        if (S < 0)
            S = 0;
        if (S > 1)
            S = 1;
        if (V < 0)
            V = 0;
        if (V > 1)
            V = 1;
        this.hue = H;
        this.saturation = S;
        this.value = V;
    }

    public double getHue() {
        return hue;
    }

    public double getSaturation() {
        return saturation;
    }

    public double getValue() {
        return value;
    }

    public int[] toRGB(){
        int[] rgb = new int[3];
        if ( saturation == 0 )
        {
            int R = (int)(value * 255);
            int G = (int)(value * 255);
            int B = (int)(value * 255);
            if (R < 0)
                R = 0;
            if (R > 255)
                R = 255;
            if (G < 0)
                G = 0;
            if (G > 255)
                G = 255;
            if (B < 0)
                B = 0;
            if (B > 255)
                B = 255;
            rgb[0] = R;
            rgb[1] = G;
            rgb[2] = B;
        }
        else
        {
            double h = hue * 6;
            if ( h == 6 )
                h = 0;      //H must be < 1
            int i = (int)h;             //Or ... var_i = floor( var_h )
            double var_1 = value * ( 1 - saturation );
            double var_2 = value * ( 1 - saturation * ( h - i ) );
            double var_3 = value * ( 1 - saturation * ( 1 - ( h - i ) ) );

            double var_r;
            double var_g;
            double var_b;
            if      ( i == 0 ) { var_r = value; var_g = var_3; var_b = var_1; }
            else if ( i == 1 ) { var_r = var_2; var_g = value; var_b = var_1; }
            else if ( i == 2 ) { var_r = var_1; var_g = value; var_b = var_3; }
            else if ( i == 3 ) { var_r = var_1; var_g = var_2; var_b = value; }
            else if ( i == 4 ) { var_r = var_3; var_g = var_1; var_b = value; }
            else               { var_r = value; var_g = var_1; var_b = var_2; }

            int R = (int)(var_r * 255);
            int G = (int)(var_g * 255);
            int B = (int)(var_b * 255);
            if (R < 0)
                R = 0;
            if (R > 255)
                R = 255;
            if (G < 0)
                G = 0;
            if (G > 255)
                G = 255;
            if (B < 0)
                B = 0;
            if (B > 255)
                B = 255;
            rgb[0] = R;
            rgb[1] = G;
            rgb[2] = B;
        }
        return rgb;
    }
}
