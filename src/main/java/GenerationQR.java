import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 *class that generates a QR code, which contains a value from a line of an input file (example .qrdata) between the first and second spaces
 (in this case LPA link to download the eSIM profile). One QR code corresponds to each line.
 * @author Ivan Emelyanenko
 * @version 1.0
 *
 */

public class GenerationQR {

    // Function to create the QR code

    /**
     * This method is used to create QR code and write it to file (.png)
     * @param data data to be placed in the QR code
     * @param path file name with QR code (in this case used .png file)
     * @param charset character encoding standard (in this case used "UTF-8")
     * @param hashMap hash table with settings for generating a QR code.
     * @param height height QR code
     * @param width width QR code
     * @throws WriterException
     * @throws IOException
     */
    public void createQR(String data, String path,
                                String charset, Map hashMap,
                                int height, int width)
            throws WriterException, IOException
    {
        // создание объекта двумерной матрицы битов (матрица QR)
        // 1 параметр - что кодируем (какую кодировку при этом используем)
        // 2 параметр -
        BitMatrix matrix = new MultiFormatWriter().encode(
                new String(data.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, width, height, hashMap );

        MatrixToImageWriter.writeToFile(
                matrix,
                path.substring(path.lastIndexOf('.') + 1),
                new File(path));
    }

    /**
     *
     * @param charset character encoding standard (in this case used "UTF-8")
     * @return hash table with settings for generating a QR code.
     */
    public Map<EncodeHintType, Object> createMapForQR(String charset){

        Map<EncodeHintType, Object> hashMap = new HashMap<EncodeHintType, Object>();
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hashMap.put(EncodeHintType.CHARACTER_SET, charset);
        return hashMap;
    }

    /**
     *
     * @param pathToQrdataFile file name with data for create QR code (in this case name .qrdata file)
     * @param mapForQR hash table with settings for generating a QR code.
     * @param height height QR code
     * @param width width QR code
     * @throws WriterException
     * @throws IOException
     */
    public void getQRCode(String pathToQrdataFile, Map<EncodeHintType, Object> mapForQR, int height, int width)throws WriterException, IOException{
         int indexNameQrdata = pathToQrdataFile.lastIndexOf(".");

         String nameDirectoryForQRCode = pathToQrdataFile.substring(0, indexNameQrdata);
         File fileWithQR = new File(nameDirectoryForQRCode);

         if (!fileWithQR.exists()){

             fileWithQR.mkdir();
         }


        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToQrdataFile))) {
            String parametersProfile = null;

            while((parametersProfile = bufferedReader.readLine())!=null){

                PreparationDataQR prepareQR = new PreparationDataQR(parametersProfile);
                prepareQR.setParameters(parametersProfile);

                String pathForQR = fileWithQR.getAbsolutePath() + "\\" + prepareQR.nameQR + ".png";
                createQR(prepareQR.LPALink, pathForQR, "UTF-8", mapForQR, height, width);
                BufferedImage imageWihQR = getQRWithSignature(pathForQR, prepareQR.signatureQR);
                writeFullQR(imageWihQR, "png", pathForQR);
            }
            bufferedReader.close();

        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }


    //Подкласс BufferedImage описывает изображение с доступным буфером данных изображения
    //Класс Java BufferedImage является подклассом класса Image. Он используется для обработки и управления данными изображения.
    // BufferedImage - класс который представляет изображение, которое хранится в памяти. С помощью этого класса мы будем обрабатывать изображения.
    // https://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferedImage.html
    // getGraphics - This method returns a Graphics2D, but is here for backwards compatibility.
    // Graphics2D - Этот класс Graphics2D расширяет класс Graphics, чтобы обеспечить более сложный контроль над геометрией, преобразованиями координат, управлением цветом и макетом текста.
    // Это фундаментальный класс для визуализации двухмерных форм, текста и изображений на платформе Java
    //https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics2D.html
    // path - из какого файла считываем, s - какую строку записываем

    /**
     *
     * @param pathToFile file name with QR code (.png)
     * @param s signature under QR code (ICCID eSIM profile)
     * @return  a class object that represents an image stored in memory (QR code).
     * @throws IOException
     */
    public BufferedImage getQRWithSignature(String pathToFile, String s)throws IOException{

        BufferedImage myPicture = ImageIO.read(new File(pathToFile));
        Graphics2D g = (Graphics2D) myPicture.getGraphics();
        g.setColor(Color.BLACK);

        int width = myPicture.getWidth()/3- 30;
        int height = myPicture.getHeight();
        //  g.drawString(s, 100, 400);
        //  private Font f1 = new Font("TimesRoman", Font.BOLD, 22),
        g.setFont(new Font("TimesRoman", Font.BOLD, 18));
        g.drawString(s, width, height);
        //освобождаем ресурсы
        g.dispose();
        return myPicture;

    }

    // ImageIO.write(imageWithText, "png", new File(path));

    /**
     *
     * @param image object that represents an image stored in memory (QR code).
     * @param format file extension  for writing QR code (in this case used .png)
     * @param pathToFile file name with QR code
     * @throws IOException
     */

    public void writeFullQR(BufferedImage image, String format, String pathToFile)throws IOException{
        ImageIO.write(image, format, new File(pathToFile));

    }

    public static void main(String[] args) throws WriterException, IOException{
       // String pathToDirectoryWithPng = args[0];
       // String pathToQrdataFileWithLPA = args[1];

        String pathToQrdataFileWithLPA = args[0];

        GenerationQR generationQR = new GenerationQR();
        Map<EncodeHintType, Object> mapSettingsQR = generationQR.createMapForQR("UTF-8");

      //  generationQR.getQRCode(pathToDirectoryWithPng, pathToQrdataFileWithLPA, mapSettingsQR, 400, 400);
              generationQR.getQRCode(pathToQrdataFileWithLPA, mapSettingsQR, 400, 400);

    }



}
