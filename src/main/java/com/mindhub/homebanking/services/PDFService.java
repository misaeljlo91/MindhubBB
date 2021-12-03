package com.mindhub.homebanking.services;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PDFService {

    public void export(HttpServletResponse response,
                       String numberDescription,
                       String client1,
                       String account1,
                       String client2,
                       String account2,
                       String type,
                       double amount,
                       String description) throws IOException;
}
