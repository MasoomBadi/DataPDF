package com.phoenix.datapdf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.phoenix.datapdf.dbhelper.FarmerDBHelper;
import com.phoenix.datapdf.modals.Agri;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DataActivity extends AppCompatActivity {

    MaterialTextView name, regno, sonof, dob, phone, aadhar, block, village, panchayat, district;
    MaterialButton savepdf;
    String id;
    Bitmap qrcode;
    private static final int CREATE_FILE = 1;
    List<Agri> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        name = findViewById(R.id.disp_name);
        regno = findViewById(R.id.disp_regno);
        sonof = findViewById(R.id.disp_sonof);
        phone = findViewById(R.id.disp_phone);
        dob = findViewById(R.id.disp_dob);
        aadhar = findViewById(R.id.disp_aadhar);
        block = findViewById(R.id.disp_block);
        village = findViewById(R.id.disp_village);
        panchayat = findViewById(R.id.disp_panchayat);
        district = findViewById(R.id.disp_district);
        savepdf = findViewById(R.id.btn_save);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("ID");
        }


        FarmerDBHelper dbHelper = new FarmerDBHelper(this);
        datalist = dbHelper.getSingleData(id);

        name.setText(getString(R.string.name, datalist.get(0).getName()));
        regno.setText(getString(R.string.regno, datalist.get(0).getRegNo()));
        sonof.setText(getString(R.string.sonof, datalist.get(0).getSonOf()));
        phone.setText(getString(R.string.phone, datalist.get(0).getPhone()));
        dob.setText(getString(R.string.dob, datalist.get(0).getDob()));
        aadhar.setText(getString(R.string.aadhar, datalist.get(0).getAadharNo()));
        block.setText(getString(R.string.block, datalist.get(0).getBlock()));
        village.setText(getString(R.string.village, datalist.get(0).getVillage()));
        panchayat.setText(getString(R.string.panchayat, datalist.get(0).getPanchayat()));
        district.setText(getString(R.string.district, datalist.get(0).getDistrict()));

        savepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void saveData() throws IOException {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, datalist.get(0).getName()+".pdf");

        startActivityForResult(intent, CREATE_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == CREATE_FILE
                && resultCode == Activity.RESULT_OK) {

            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                try {
                    ParcelFileDescriptor pfd = this.getContentResolver().
                            openFileDescriptor(uri, "w");

                    generateQrCode(datalist.get(0).getName(), datalist.get(0).getRegNo());

                    Document document = new Document(PageSize.A4, 0, 8, 0, 0);
                    FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
                    PdfWriter.getInstance(document, fileOutputStream);
                    document.open();
                    document.add(new Chunk());

                    int indentation = 0;
                    InputStream ins = getAssets().open("header.png");
                    Bitmap bitmap = BitmapFactory.decodeStream(ins);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Image image = Image.getInstance(stream.toByteArray());


                    float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                            - document.rightMargin() - indentation) / image.getWidth()) * 80;

                    image.scalePercent(scaler);


                    PdfPTable headerTable = new PdfPTable(1);
                    headerTable.setTotalWidth(595f);
                    float[] headerWidth = new float[]{595};

                    headerTable.setWidths(headerWidth);
                    headerTable.setLockedWidth(true);

                    PdfPCell logoCell = new PdfPCell(image, true);
                    logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    logoCell.setBorder(Rectangle.NO_BORDER);


                    headerTable.addCell(logoCell);

                    Font SUBFONT = new Font(Font.getFamily("TIMES_ROMAN"), 22,    Font.BOLD|Font.UNDERLINE);
                    Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                    Font normalfont = new Font(Font.FontFamily.HELVETICA, 18);
                    Paragraph nameP = new Paragraph(new Phrase(22f, "Farmer Registration", SUBFONT));
                    nameP.setSpacingBefore(20f);
                    nameP.setAlignment(Element.ALIGN_CENTER);

                    PdfPTable detailTable = new PdfPTable(2);
                    detailTable.setTotalWidth(595f);
                    float[] detailWidth = new float[]{400,195};

                    detailTable.setWidths(detailWidth);
                    detailTable.setLockedWidth(true);
                    detailTable.setSpacingBefore(30f);

                    PdfPCell detailCell = new PdfPCell();
                    detailCell.setRowspan(5);

                    Paragraph personNameP = new Paragraph(new Phrase(9f, getString(R.string.name,
                            datalist.get(0).getName()), titleFont));
                    personNameP.setAlignment(Element.ALIGN_LEFT);
                    personNameP.setSpacingAfter(20);

                    Paragraph sonOfP = new Paragraph(new Phrase(9f, getString(R.string.sonof,
                            datalist.get(0).getSonOf()), normalfont));
                    sonOfP.setAlignment(Element.ALIGN_LEFT);
                    sonOfP.setSpacingAfter(20);

                    Paragraph dob = new Paragraph(new Phrase(9f, getString(R.string.dob,
                            datalist.get(0).getDob()), normalfont));
                    dob.setAlignment(Element.ALIGN_LEFT);
                    dob.setSpacingAfter(20);

                    Paragraph phone = new Paragraph(new Phrase(9f, getString(R.string.phone,
                            datalist.get(0).getPhone()), normalfont));

                    phone.setAlignment(Element.ALIGN_LEFT);
                    phone.setSpacingAfter(20);

                    Paragraph aadhar = new Paragraph(new Phrase(9f, getString(R.string.aadhar,
                            datalist.get(0).getAadharNo()), normalfont));

                    aadhar.setAlignment(Element.ALIGN_LEFT);
                    aadhar.setSpacingAfter(20);

                    detailCell.setPaddingLeft(20f);
                    detailCell.setPaddingTop(20f);
                    detailCell.addElement(personNameP);
                    detailCell.addElement(sonOfP);
                    detailCell.addElement(dob);
                    detailCell.addElement(phone);
                    detailCell.addElement(aadhar);
                    detailCell.setVerticalAlignment(Element.ALIGN_LEFT);
                    detailCell.setBorder(Rectangle.NO_BORDER);


                    ByteArrayOutputStream qrcodestream = new ByteArrayOutputStream();
                    qrcode.compress(Bitmap.CompressFormat.PNG, 100, qrcodestream);
                    Image qrImg = Image.getInstance(qrcodestream.toByteArray());
                    qrImg.setAlignment(Element.ALIGN_CENTER);

                    PdfPCell qrCell = new PdfPCell(qrImg, true);
                    qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    qrCell.setRowspan(1);
                    qrCell.setPaddingRight(30);
                    qrCell.setPaddingTop(15);
                    qrCell.setBorder(Rectangle.NO_BORDER);

                    detailTable.addCell(detailCell);
                    detailTable.addCell(qrCell);

                    PdfPTable regnotable = new PdfPTable(1);
                    regnotable.setTotalWidth(595f);
                    float[] regnowidth = new float[]{595};

                    regnotable.setWidths(regnowidth);
                    regnotable.setLockedWidth(true);
                    regnotable.setSpacingBefore(25f);

                    PdfPCell regCell = new PdfPCell();
                    regCell.setRowspan(1);
                    regCell.setBorder(Rectangle.NO_BORDER);
                    regCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    regCell.setVerticalAlignment(Element.ALIGN_CENTER);

                    Paragraph regNoP = new Paragraph(new Phrase(datalist.get(0).getRegNo(), titleFont));

                    regNoP.setAlignment(Element.ALIGN_CENTER);
                    regCell.addElement(regNoP);

                    regnotable.addCell(regCell);
                    regnotable.setSpacingAfter(15);

                    LineSeparator ls = new LineSeparator();

                    PdfPTable cc = new PdfPTable(1);
                    cc.setTotalWidth(595f);
                    float[] ccwidth = new float[]{595};

                    cc.setSpacingBefore(10);

                    cc.setWidths(ccwidth);
                    cc.setLockedWidth(true);

                    InputStream incc = getAssets().open("ccno.png");
                    Bitmap ccbit = BitmapFactory.decodeStream(incc);

                    ByteArrayOutputStream ccstream = new ByteArrayOutputStream();
                    ccbit.compress(Bitmap.CompressFormat.PNG, 100, ccstream);
                    Image ccimage = Image.getInstance(ccstream.toByteArray());

                    PdfPCell ccCell = new PdfPCell(ccimage, true);
                    ccCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    ccCell.setVerticalAlignment(Element.ALIGN_CENTER);
                    ccCell.setBorder(Rectangle.NO_BORDER);



                    cc.addCell(ccCell);

                    document.add(new Chunk());

                    InputStream ins2 = getAssets().open("header2.png");
                    Bitmap bitmap2 = BitmapFactory.decodeStream(ins2);

                    ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                    bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                    Image image2 = Image.getInstance(stream2.toByteArray());


                    image2.scalePercent(scaler);


                    PdfPTable headerTable2 = new PdfPTable(1);
                    headerTable2.setTotalWidth(595f);
                    float[] headerWidth2 = new float[]{595f};
                    headerTable2.setSpacingBefore(20);
                    headerTable2.setWidths(headerWidth2);
                    headerTable2.setLockedWidth(true);

                    PdfPCell logoCell2 = new PdfPCell(image2, true);
                    logoCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    logoCell2.setBorder(Rectangle.NO_BORDER);


                    headerTable2.addCell(logoCell2);

                    PdfPTable detailTable2 = new PdfPTable(2);
                    detailTable2.setTotalWidth(595f);
                    float[] detailWidth2 = new float[]{400,195};

                    detailTable2.setWidths(detailWidth2);
                    detailTable2.setLockedWidth(true);
                    detailTable2.setSpacingBefore(15f);

                    PdfPCell detailCell2 = new PdfPCell();
                    detailCell2.setRowspan(5);

                    Paragraph addressTitleP2 = new Paragraph(new Phrase("Address:", SUBFONT));
                    addressTitleP2.setAlignment(Element.ALIGN_LEFT);
                    addressTitleP2.setSpacingAfter(15);
                    addressTitleP2.setSpacingBefore(15);

                    Paragraph blockP = new Paragraph(new Phrase(9f, getString(R.string.block,
                            datalist.get(0).getBlock()), normalfont));
                    blockP.setAlignment(Element.ALIGN_LEFT);
                    blockP.setSpacingAfter(15);

                    Paragraph villageP = new Paragraph(new Phrase(9f, getString(R.string.village,
                            datalist.get(0).getVillage()), normalfont));
                    villageP.setAlignment(Element.ALIGN_LEFT);
                    villageP.setSpacingAfter(15);

                    Paragraph panchayatP = new Paragraph(new Phrase(9f, getString(R.string.panchayat,
                            datalist.get(0).getPanchayat()), normalfont));

                    panchayatP.setAlignment(Element.ALIGN_LEFT);
                    panchayatP.setSpacingAfter(15);

                    Paragraph districtP = new Paragraph(new Phrase(9f, getString(R.string.district,
                            datalist.get(0).getDistrict()), normalfont));

                    districtP.setAlignment(Element.ALIGN_LEFT);
                    districtP.setSpacingAfter(15);

                    detailCell2.setPaddingLeft(20f);
                    detailCell2.setPaddingTop(20f);
                    detailCell2.addElement(addressTitleP2);
                    detailCell2.addElement(blockP);
                    detailCell2.addElement(villageP);
                    detailCell2.addElement(panchayatP);
                    detailCell2.addElement(districtP);
                    detailCell2.setVerticalAlignment(Element.ALIGN_LEFT);
                    detailCell2.setBorder(Rectangle.NO_BORDER);



                    PdfPCell qrCell2 = new PdfPCell(qrImg, true);
                    qrCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    qrCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    qrCell2.setRowspan(1);
                    qrCell2.setPaddingRight(30);
                    qrCell2.setPaddingTop(40);
                    qrCell2.setBorder(Rectangle.NO_BORDER);

                    detailTable2.addCell(detailCell2);
                    detailTable2.addCell(qrCell2);

                    PdfPTable detailTable3 = new PdfPTable(2);
                    detailTable3.setTotalWidth(595f);
                    float[] detailWidth3 = new float[]{150,445};

                    detailTable3.setWidths(detailWidth3);
                    detailTable3.setLockedWidth(true);
                    detailTable3.setSpacingBefore(15f);

                    InputStream ins3 = getAssets().open("logo.jpg");
                    Bitmap bitmap3 = BitmapFactory.decodeStream(ins3);

                    ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
                    bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, stream3);
                    Image image3 = Image.getInstance(stream3.toByteArray());

                    PdfPCell imgCell = new PdfPCell(image3, true);
                    imgCell.setPaddingLeft(20);
                    imgCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    imgCell.setBorder(Rectangle.NO_BORDER);
                    imgCell.setPaddingBottom(5);

                    PdfPCell regCell2 = new PdfPCell();
                    regCell2.setRowspan(1);
                    regCell2.setBorder(Rectangle.NO_BORDER);
                    regCell2.setHorizontalAlignment(Element.ALIGN_CENTER | Element.ALIGN_MIDDLE);
                    regCell2.setVerticalAlignment(Element.ALIGN_CENTER | Element.ALIGN_MIDDLE);

                    Paragraph regNoP2 = new Paragraph(new Phrase(datalist.get(0).getRegNo(), titleFont));

                    regNoP2.setAlignment(Element.ALIGN_MIDDLE);
                    regNoP2.setAlignment(Element.ALIGN_CENTER);
                    regCell2.addElement(regNoP2);

                    detailTable3.addCell(imgCell);
                    detailTable3.addCell(regCell2);
                    detailTable3.setSpacingAfter(5);

                    PdfPTable cc2 = new PdfPTable(1);
                    cc2.setTotalWidth(595f);
                    float[] ccwidth2 = new float[]{595};

                    cc2.setSpacingBefore(10);

                    cc2.setWidths(ccwidth2);
                    cc2.setLockedWidth(true);

                    InputStream incc2 = getAssets().open("footer.png");
                    Bitmap ccbit2 = BitmapFactory.decodeStream(incc2);

                    ByteArrayOutputStream ccstream2 = new ByteArrayOutputStream();
                    ccbit2.compress(Bitmap.CompressFormat.PNG, 100, ccstream2);
                    Image ccimage2 = Image.getInstance(ccstream2.toByteArray());

                    PdfPCell ccCell2 = new PdfPCell(ccimage2, true);
                    ccCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    ccCell2.setVerticalAlignment(Element.ALIGN_CENTER);
                    ccCell2.setBorder(Rectangle.NO_BORDER);

                    cc2.addCell(ccCell2);


                    document.add(headerTable);
                    document.add(nameP);
                    document.add(detailTable);
                    document.add(regnotable);
                    document.add(ls);
                    document.add(cc);
                    document.newPage();
                    document.add(headerTable2);
                    document.add(detailTable2);
                    document.add(detailTable3);
                    document.add(ls);
                    document.add(cc2);

                    document.close();
                } catch (DocumentException | IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "File not found!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    void generateQrCode(String name, String regNumber) {
        MultiFormatWriter formatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = formatWriter.encode("Name: " + name +
                            "\nReg No: " + regNumber
                    , BarcodeFormat.QR_CODE, 350, 350);

            BarcodeEncoder encoder = new BarcodeEncoder();
            qrcode = encoder.createBitmap(bitMatrix);

        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to generate barcode", Toast.LENGTH_SHORT).show();
        }
    }
}