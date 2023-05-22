
package com.crio.warmup.stock.dto;

import java.util.Comparator;
//import org.springframework.util.comparator.ComparableComparator;

public class TotalReturnsDto 
{

  public static final Comparator<TotalReturnsDto> closingComaparator=new Comparator<>()
  {
    public int compare(TotalReturnsDto t1,TotalReturnsDto t2)
    {
      return (int) (t1.getClosingPrice().compareTo(t2.getClosingPrice()));
    }
  };


  private String symbol;
  private Double closingPrice;

  public TotalReturnsDto(String symbol, Double closingPrice) {
    this.symbol = symbol;
    this.closingPrice = closingPrice;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public Double getClosingPrice() {
    return closingPrice;
  }

  public void setClosingPrice(Double closingPrice) {
    this.closingPrice = closingPrice;
  }
}
