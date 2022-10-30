package main.java.br.com.webserver.service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import static main.java.br.com.webserver.service.Pages.getPathIfPageExist;

public final class HttpRequest implements Runnable {
    Socket socket;

    private static final String CRF = "\r\n";

    public HttpRequest(Socket socket) throws Exception {
        this.socket = socket;
    }

    // Implement the run() method of the Runnable interface.
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void processRequest() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            OutputStream out = socket.getOutputStream();
            String input = getInputFileName(in);
            writeOutPage(input, out);
            in.close();
            out.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeOutPage(String input, OutputStream out) throws IOException {
        String pathNameOut = getPathIfPageExist(input);
        String pathProject = System.getProperty("user.dir");
        File arquivo = new File(pathProject + pathNameOut);

        try {
            if (pathNameOut.contains("image")) {
                String formatFile = pathNameOut.split("\\.")[1];
                String toStringing = "HTTP/1.0 200 OK\r\nContent-Type: image/" +formatFile+ "\r\nConnection: close\r\n\r\n";
                BufferedImage image = ImageIO.read(arquivo);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byteArrayOutputStream.write(toStringing.getBytes());
                ImageIO.write(image, formatFile, byteArrayOutputStream);
                byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
                byteArrayOutputStream.write(CRF.getBytes());
                out.write(byteArrayOutputStream.toByteArray());
                out.flush();
                out.close();
            } else {
                PrintWriter printWriter = new PrintWriter(out);
                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(new FileInputStream(arquivo)));
                String saidaLinha = buffer.lines().collect(Collectors.joining("\n"));
                String saida = saidaLinha + "\n\r\n\r";

                if (pathNameOut.contains("error")) {
                    printWriter.print("HTTP/1.0 404 NOT FOUND\r\nContent-Type: text/html\r\nConnection: close\r\n\r\n");
                    printWriter.print(saida);

                } else {
                    printWriter.print("HTTP/1.0 200 OK\r\nContent-Type: text/html\r\nConnection: close\r\n\r\n");
                    printWriter.print(saida);

                }
                printWriter.flush();
                printWriter.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private String getInputFileName(BufferedReader in) throws IOException {
        StringBuffer request = new StringBuffer();
        String input;
        while ((input = in.readLine()).length() != 0) {
            request.append(input).append(" ");
        }
        System.out.println(request.toString());
        StringTokenizer value = new StringTokenizer(request.toString());
        value.nextToken();
        return value.nextToken();
    }
}