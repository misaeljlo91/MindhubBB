package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository, CreditCardRepository creditCardRepository, AccountCardRepository accountCardRepository) {
		return (args) -> {
			Client client1 = new Client("Melba","Morel","melba.morel123","melba@mindhub.com",passwordEncoder.encode("melba123"),"/web/assets/melba.jpg",ClientRole.USER);
			Client client2 = new Client("Misael J.","López","misaeljlo91","misaeljlo91@gmail.com",passwordEncoder.encode("Mleojose18"),"/web/assets/misael.jpg",ClientRole.ADMIN);

			int minAccount = 100000;
			int maxAccount = 999999;

			int minID = 10;
			int maxID = 99;

            Account account1 = new Account(client1,"VIN-000001/18",AccountType.Checking,LocalDateTime.now(),5000);
			Account account2 = new Account(client1,"VIN-000002/29",AccountType.Savings,LocalDateTime.now().plusDays(1),7500);

			//Account account1 = new Account(client1,"VIN-"+(int)((Math.random()*(maxAccount-minAccount+1))+minAccount)+"/"+(int)((Math.random()*(maxID-minID+1))+minID),AccountType.Checking,LocalDateTime.now(),5000);
			//Account account2 = new Account(client1,"VIN-"+(int)((Math.random()*(maxAccount-minAccount+1))+minAccount)+"/"+(int)((Math.random()*(maxID-minID+1))+minID),AccountType.Savings,LocalDateTime.now().plusDays(1),7500);
			Account account3 = new Account(client2,"VIN-"+(int)((Math.random()*(maxAccount-minAccount+1))+minAccount)+"/"+(int)((Math.random()*(maxID-minID+1))+minID),AccountType.Checking,LocalDateTime.now(),8000);
			Account account4 = new Account(client2,"VIN-"+(int)((Math.random()*(maxAccount-minAccount+1))+minAccount)+"/"+(int)((Math.random()*(maxID-minID+1))+minID),AccountType.Savings,LocalDateTime.now().plusDays(1),6500);

			int minDescription = 100000;
			int maxDescription = 999999;

			int minIdDescription = 100;
			int maxIdDescription = 999;

			Transaction transaction1 = new Transaction(account1, account1.getHolder(), account1.getNumber(), TransactionType.Debit,400,"Purchase","D"+(int)((Math.random()*(maxDescription-minDescription+1))+minDescription)+"/"+(int)((Math.random()*(maxIdDescription-minIdDescription+1))+minIdDescription), account2.getHolder(), account2.getNumber(),3900, LocalDateTime.now().minusDays(2).minusHours(3));
			Transaction transaction2 = new Transaction(account1, account1.getHolder(), account1.getNumber(), TransactionType.Credit,600,"Deposit","C"+(int)((Math.random()*(maxDescription-minDescription+1))+minDescription)+"/"+(int)((Math.random()*(maxIdDescription-minIdDescription+1))+minIdDescription), account3.getHolder(), account3.getNumber(),4500, LocalDateTime.now().minusDays(1));
			Transaction transaction3 = new Transaction(account1, account1.getHolder(), account1.getNumber(), TransactionType.Credit,500,"Deposit","C"+(int)((Math.random()*(maxDescription-minDescription+1))+minDescription)+"/"+(int)((Math.random()*(maxIdDescription-minIdDescription+1))+minIdDescription), account4.getHolder(), account4.getNumber(),5000, LocalDateTime.now());
			Transaction transaction4 = new Transaction(account2, account2.getHolder(), account2.getNumber(), TransactionType.Credit,1000,"Deposit","C"+(int)((Math.random()*(maxDescription-minDescription+1))+minDescription)+"/"+(int)((Math.random()*(maxIdDescription-minIdDescription+1))+minIdDescription), account1.getHolder(), account1.getNumber(),8400, LocalDateTime.now().minusDays(3).minusHours(8));
			Transaction transaction5 = new Transaction(account2, account2.getHolder(), account2.getNumber(), TransactionType.Debit,300,"Purchase","D"+(int)((Math.random()*(maxDescription-minDescription+1))+minDescription)+"/"+(int)((Math.random()*(maxIdDescription-minIdDescription+1))+minIdDescription), account3.getHolder(), account3.getNumber(),8100, LocalDateTime.now().minusHours(6));
			Transaction transaction6 = new Transaction(account2, account2.getHolder(), account2.getNumber(), TransactionType.Debit,600,"Withdrawal","D"+(int)((Math.random()*(maxDescription-minDescription+1))+minDescription)+"/"+(int)((Math.random()*(maxIdDescription-minIdDescription+1))+minIdDescription), account4.getHolder(), account4.getNumber(),7500, LocalDateTime.now());
			Transaction transaction7 = new Transaction(account3, account3.getHolder(), account3.getNumber(), TransactionType.Debit,800,"Transfer","D"+(int)((Math.random()*(maxDescription-minDescription+1))+minDescription)+"/"+(int)((Math.random()*(maxIdDescription-minIdDescription+1))+minIdDescription), account1.getHolder(), account1.getNumber(),7200, LocalDateTime.now().minusDays(5).minusHours(2));
			Transaction transaction8 = new Transaction(account3, account3.getHolder(), account3.getNumber(), TransactionType.Debit,200,"Purchase","D"+(int)((Math.random()*(maxDescription-minDescription+1))+minDescription)+"/"+(int)((Math.random()*(maxIdDescription-minIdDescription+1))+minIdDescription), account2.getHolder(), account2.getNumber(),7000, LocalDateTime.now().minusDays(2).minusHours(4));
			Transaction transaction9 = new Transaction(account3, account3.getHolder(), account3.getNumber(), TransactionType.Credit,1000,"Transfer","C"+(int)((Math.random()*(maxDescription-minDescription+1))+minDescription)+"/"+(int)((Math.random()*(maxIdDescription-minIdDescription+1))+minIdDescription), account4.getHolder(), account4.getNumber(),8000, LocalDateTime.now());
			Transaction transaction10 = new Transaction(account4, account4.getHolder(), account4.getNumber(), TransactionType.Debit,700,"Withdrawal","D"+(int)((Math.random()*(maxDescription-minDescription+1))+minDescription)+"/"+(int)((Math.random()*(maxIdDescription-minIdDescription+1))+minIdDescription), account1.getHolder(), account1.getNumber(),4600, LocalDateTime.now().minusDays(5).minusHours(5));
			Transaction transaction11 = new Transaction(account4, account4.getHolder(), account4.getNumber(), TransactionType.Credit,1000,"Transfer","C"+(int)((Math.random()*(maxDescription-minDescription+1))+minDescription)+"/"+(int)((Math.random()*(maxIdDescription-minIdDescription+1))+minIdDescription), account2.getHolder(), account2.getNumber(),5600, LocalDateTime.now().minusDays(3));
			Transaction transaction12 = new Transaction(account4, account4.getHolder(), account4.getNumber(), TransactionType.Credit,900,"Deposit","C"+(int)((Math.random()*(maxDescription-minDescription+1))+minDescription)+"/"+(int)((Math.random()*(maxIdDescription-minIdDescription+1))+minIdDescription), account3.getHolder(), account3.getNumber(),6500, LocalDateTime.now());

            Loan loan1 = new Loan("Mortgage",500000,5, List.of(12,24,36,48,60));
			Loan loan2 = new Loan("Personal",100000,20, List.of(6,12,24));
			Loan loan3 = new Loan("Car",300000,10, List.of(6,12,24,36));

			ClientLoan clientLoan1 = new ClientLoan(client1, loan1, loan1.getName(),400000, 60, 6666.67);
			ClientLoan clientLoan2 = new ClientLoan(client1, loan2, loan2.getName(), 50000, 12, 4166.67);
			ClientLoan clientLoan3 = new ClientLoan(client2, loan2, loan2.getName(),100000, 24, 4166.67);
			ClientLoan clientLoan4 = new ClientLoan(client2, loan3, loan3.getName(),200000, 36, 5555.56);

			int minCard = 1000;
			int maxCard = 9999;

			int minCVV = 100;
			int maxCVV = 999;

			Card card1 = new Card(client1, CardType.Debit,(int)((Math.random()*(maxCard-minCard+1))+minCard)+" "+(int)((Math.random()*(maxCard-minCard+1))+minCard)+" "+((int)(Math.random()*(maxCard-minCard+1))+minCard)+" "+(int)((Math.random()*(maxCard-minCard+1))+minCard),(int)((Math.random()*(maxCVV-minCVV+1))+minCVV), LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			Card card2 = new Card(client1, CardType.Debit,(int)((Math.random()*(maxCard-minCard+1))+minCard)+" "+(int)((Math.random()*(maxCard-minCard+1))+minCard)+" "+((int)(Math.random()*(maxCard-minCard+1))+minCard)+" "+(int)((Math.random()*(maxCard-minCard+1))+minCard),(int)((Math.random()*(maxCVV-minCVV+1))+minCVV), LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			Card card3 = new Card(client2, CardType.Debit,(int)((Math.random()*(maxCard-minCard+1))+minCard)+" "+(int)((Math.random()*(maxCard-minCard+1))+minCard)+" "+((int)(Math.random()*(maxCard-minCard+1))+minCard)+" "+(int)((Math.random()*(maxCard-minCard+1))+minCard),(int)((Math.random()*(maxCVV-minCVV+1))+minCVV), LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			Card card4 = new Card(client2, CardType.Debit,(int)((Math.random()*(maxCard-minCard+1))+minCard)+" "+(int)((Math.random()*(maxCard-minCard+1))+minCard)+" "+((int)(Math.random()*(maxCard-minCard+1))+minCard)+" "+(int)((Math.random()*(maxCard-minCard+1))+minCard),(int)((Math.random()*(maxCVV-minCVV+1))+minCVV), LocalDateTime.now().minusYears(5), LocalDateTime.now());

			CreditCard creditCard1 = new CreditCard(client1, CardType.Credit, CardColor.Titanium,(int)((Math.random()*(maxCard-minCard+1))+minCard)+" "+(int)((Math.random()*(maxCard-minCard+1))+minCard)+" "+((int)(Math.random()*(maxCard-minCard+1))+minCard)+" "+(int)((Math.random()*(maxCard-minCard+1))+minCard),(int)((Math.random()*(maxCVV-minCVV+1))+minCVV), 30000, 0, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			CreditCard creditCard2 = new CreditCard(client2, CardType.Credit, CardColor.Gold,(int)((Math.random()*(maxCard-minCard+1))+minCard)+" "+(int)((Math.random()*(maxCard-minCard+1))+minCard)+" "+((int)(Math.random()*(maxCard-minCard+1))+minCard)+" "+(int)((Math.random()*(maxCard-minCard+1))+minCard),(int)((Math.random()*(maxCVV-minCVV+1))+minCVV), 15000, 0, LocalDateTime.now(), LocalDateTime.now().plusYears(5));

			AccountCard accountCard1 = new AccountCard(account1, card1, account1.getNumber(), card1.getNumber(), card1.getCvv(), account1.getBalance());
			AccountCard accountCard2 = new AccountCard(account2, card2, account2.getNumber(), card2.getNumber(), card2.getCvv(), account2.getBalance());
			AccountCard accountCard3 = new AccountCard(account3, card3, account3.getNumber(), card3.getNumber(), card3.getCvv(), account3.getBalance());
			AccountCard accountCard4 = new AccountCard(account4, card4, account4.getNumber(), card4.getNumber(), card4.getCvv(), account4.getBalance());

			clientRepository.save(client1);
			clientRepository.save(client2);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);
			transactionRepository.save(transaction9);
			transactionRepository.save(transaction10);
			transactionRepository.save(transaction11);
			transactionRepository.save(transaction12);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
			cardRepository.save(card4);

			creditCardRepository.save(creditCard1);
			creditCardRepository.save(creditCard2);

			accountCardRepository.save(accountCard1);
			accountCardRepository.save(accountCard2);
			accountCardRepository.save(accountCard3);
			accountCardRepository.save(accountCard4);
		};
	}
}