package com.capgemini.pecunia.service;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.pecunia.dao.AccountDao;
import com.capgemini.pecunia.dao.ChequeDAO;
import com.capgemini.pecunia.dao.TransactionDAO;
import com.capgemini.pecunia.bean.Account;
import com.capgemini.pecunia.bean.Cheque;
import com.capgemini.pecunia.bean.Transaction;

@Service
public class CreditServiceImpl implements CreditService {
    @Autowired
    TransactionDAO tdao;
    @Autowired
	ChequeDAO cdao;
    @Autowired
    AccountDao adao;
    public void setTdao(TransactionDAO tdao) { this.tdao=tdao;} 
    public void setCdao(ChequeDAO cdao){this.cdao=cdao;}
    public void setAdao(AccountDao adao) {this.adao = adao;}
	
    @Override
	@Transactional
    public Transaction creditUsingCheque(Cheque cheque)
    {
    	Transaction t = new Transaction();
    	Account acc= adao.getAccountByAccnum(cheque.getAccount_id());
    	acc.setAccountBalance(acc.getAccountBalance()+cheque.getAmount());
    	cheque.setStatus("Success");
    	cdao.save(cheque);
    	cdao.flush();
    	t.setTransactionId(Long.parseLong(String.valueOf(Math.abs(new Random().nextLong())).substring(0, 12)));
    	t.setAmount(cheque.getAmount());
    	t.setType("Credit");
    	t.setAccId(cheque.getAccount_id());
    	LocalDate localDate = LocalDate.now();
    	t.setDateOfTrans(localDate);
    	t.setCheque(cheque);
    	
    		return tdao.save(t);
    }
}
