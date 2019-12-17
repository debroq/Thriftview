package com.debroq.tspconnect;

import java.math.BigDecimal;

import android.util.Log;

public class Fund {
	private static final String TAG = Fund.class.getName();
	String fundName;
	String shares;
	String sharePrice;
	String balance;
	String distOfAcct;
	String contAlloc;
	String traditional;
	String taxExempt;
	String roth;
	String agencyAuto;
	String agencyMatch;
	
	public void setName(String nam) {
		fundName = nam;
	}

	public void setShares(String sh) {
		shares = sh;
	}

	public void setSharePrice(String sp) {
		sharePrice = sp;
	}
	
	public void setBalance(String bal) {
		balance = bal;
	}
	
	public void setDistOfAcct(String dist) {
		distOfAcct = dist;
	}
	
	public void setContAlloc(String ca) {
		contAlloc = ca;
	}
	
	public void setTraditional(String trad) {
		traditional = trad;
	}
	
	public void setTaxExempt(String te) {
		taxExempt = te;
	}
	
	public void setRoth(String r) {
		roth = r;
	}
	
	public void setAgencyAuto(String aa) {
		agencyAuto = aa;
	}
	
	public void setAgencyMatch(String am) {
		agencyMatch = am;
	}

	public String getName() {
		return fundName;
	}

	public String getShares() {
		return shares;
	}
	
	public String getSharePrice() {
		return sharePrice;
	}
	
	public String getBalance() {
		return balance;
	}
	
	public double getBalanceDouble() {
		return Double.valueOf(balance);
	}
	
	public BigDecimal getBalanceValue() {
		double dval = Double.valueOf(balance);
		return BigDecimal.valueOf(dval).setScale(2);
	}
	
	public String getDistOfAcct() {
		return distOfAcct;
	}
	
	public String getContAlloc() {
		return contAlloc;
	}

	public double getContAllocDouble() {
Log.i(TAG, "getContAllocDouble " + contAlloc + " = " + Double.valueOf(contAlloc));
		return Double.valueOf(contAlloc);
	}

	public String getTraditional() {
		return traditional;
	}
	
	public String getTaxExempt() {
		return taxExempt;
	}
	
	public String getRoth() {
		return roth;
	}
	
	public String getAgencyAuto() {
		return agencyAuto;
	}
	
	public String getAgencyMatch() {
		return agencyMatch;
	}

}
