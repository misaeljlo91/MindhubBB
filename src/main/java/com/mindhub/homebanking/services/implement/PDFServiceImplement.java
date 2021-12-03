package com.mindhub.homebanking.services.implement;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.services.PDFService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class PDFServiceImplement implements PDFService {

    @Override
    public void export(HttpServletResponse response,
                       String numberDescription,
                       String client1,
                       String account1,
                       String client2,
                       String account2,
                       String type,
                       double amount,
                       String description) throws IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        //double doubleAmount = Double.parseDouble(amount);
        Locale locale = new Locale("en", "US");
        NumberFormat currency = NumberFormat.getCurrencyInstance(locale);
        String amountFormat = currency.format(amount);
        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, YYYY - HH:mm");
        String datePage = dateFormat.format(new Date());

        //HEADER
        document.open();
        Image image = Image.getInstance("./src/main/resources/static/web/assets/logo-card.png");
        image.scaleAbsolute(70,60);
        image.setAbsolutePosition(40,700);
        document.add(image);

        Font fontNumberDescription = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontNumberDescription.setSize(20);
        Paragraph number = new Paragraph(numberDescription, fontNumberDescription);
        number.setAlignment(Paragraph.ALIGN_TOP);
        number.setAlignment(Paragraph.ALIGN_RIGHT);
        number.setSpacingAfter(15);
        number.setSpacingBefore(0);
        document.add(number);

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Color colorFont = new Color(12, 40, 58);
        fontTitle.setSize(25);
        fontTitle.setColor(colorFont);
        Paragraph title = new Paragraph("MindHub Brothers Bank, Inc.", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        Font fontSubtitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontSubtitle.setSize(20);
        Paragraph subtitle = new Paragraph(type+" "+"transaction voucher", fontSubtitle);
        subtitle.setAlignment(Paragraph.ALIGN_CENTER);
        subtitle.setSpacingAfter(20);
        subtitle.setSpacingBefore(20);
        document.add(subtitle);

        Font fontDate = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontDate.setSize(12);
        Paragraph date = new Paragraph("Date: "+datePage, fontDate);
        date.setAlignment(Paragraph.ALIGN_RIGHT);
        date.setSpacingAfter(20);
        document.add(date);

        Font fontAmount = FontFactory.getFont(FontFactory.HELVETICA);
        Color colorAmount = new Color(155, 155, 155);
        fontAmount.setSize(18);
        fontAmount.setColor(colorAmount);
        Paragraph titleAmount = new Paragraph("Amount:", fontAmount);
        titleAmount.setFirstLineIndent(25);
        document.add(titleAmount);

        Font fontAmountNumber = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontAmountNumber.setSize(24);
        Paragraph amountNumber = new Paragraph(amountFormat, fontAmountNumber);
        amountNumber.setFirstLineIndent(40);
        amountNumber.setSpacingAfter(20);
        document.add(amountNumber);

        Font titleField = new Font(Font.HELVETICA, 18, Font.NORMAL, new Color(155, 155, 155));
        Font field = new Font(Font.HELVETICA, 18, Font.BOLD);

        Paragraph holder = new Paragraph("Holder", titleField);
        holder.setFirstLineIndent(75);
        holder.setSpacingAfter(0);
        document.add(holder);

        Paragraph holderName = new Paragraph(client1, field);
        holderName.setFirstLineIndent(75);
        holderName.setSpacingBefore(0);
        holderName.setSpacingAfter(0);
        document.add(holderName);

        Paragraph originAccount = new Paragraph("Origin account", titleField);
        originAccount.setFirstLineIndent(75);
        originAccount.setSpacingAfter(0);
        document.add(originAccount);

        Paragraph accountNumber1 = new Paragraph(account1, field);
        accountNumber1.setFirstLineIndent(75);
        accountNumber1.setSpacingBefore(0);
        accountNumber1.setSpacingAfter(20);
        document.add(accountNumber1);

        Paragraph holder2 = new Paragraph("Full name", titleField);
        holder2.setFirstLineIndent(75);
        holder2.setSpacingAfter(0);
        document.add(holder2);

        Paragraph holderName2 = new Paragraph(client2, field);
        holderName2.setFirstLineIndent(75);
        holderName2.setSpacingBefore(0);
        holderName2.setSpacingAfter(0);
        document.add(holderName2);

        Paragraph destinationAccount = new Paragraph("Destination account", titleField);
        destinationAccount.setFirstLineIndent(75);
        destinationAccount.setSpacingAfter(0);
        document.add(destinationAccount);

        Paragraph accountNumber2 = new Paragraph(account2, field);
        accountNumber2.setFirstLineIndent(75);
        accountNumber2.setSpacingBefore(0);
        accountNumber2.setSpacingAfter(20);
        document.add(accountNumber2);

        Paragraph transactionDescription = new Paragraph("Description", titleField);
        transactionDescription.setFirstLineIndent(75);
        transactionDescription.setSpacingAfter(0);
        document.add(transactionDescription);

        Paragraph descriptionText = new Paragraph(description, field);
        descriptionText.setFirstLineIndent(75);
        descriptionText.setSpacingBefore(0);
        descriptionText.setSpacingAfter(20);
        document.add(descriptionText);

        document.close();
    }
}
