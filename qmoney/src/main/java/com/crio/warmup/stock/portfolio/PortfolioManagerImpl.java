
package com.crio.warmup.stock.portfolio;

//import static java.time.temporal.ChronoUnit.DAYS;
//import static java.time.temporal.ChronoUnit.SECONDS;
import com.crio.warmup.stock.PortfolioManagerApplication;
// import static java.time.temporal.ChronoUnit.DAYS;
// import static java.time.temporal.ChronoUnit.SECONDS;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
//import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;import java.util.Comparator;
//import java.util.Comparator;
import java.util.List;
// import java.util.concurrent.ExecutionException;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;
// import java.util.concurrent.Future;
// import java.util.concurrent.TimeUnit;
// import java.util.stream.Collectors;
import com.crio.warmup.stock.quotes.StockQuotesService;
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import java.time.LocalDate;
// import java.time.temporal.ChronoUnit;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Comparator;
// import java.util.List;
// import java.util.concurrent.ExecutionException;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;
// import java.util.concurrent.Future;
// import java.util.concurrent.TimeUnit;
// import java.util.stream.Collectors;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.crio.warmup.stock.quotes.StockQuotesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager {


  private RestTemplate restTemplate;
  private StockQuotesService stockQuotesService;

  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException 
  {
        String uri=buildUri(symbol,from,to);
        RestTemplate restTemplate=new RestTemplate();
        Candle[] candles=restTemplate.getForObject(uri, TiingoCandle[].class);      
         return Arrays.asList(candles);
  }

 protected  String buildUri(String symbol, LocalDate startDate, LocalDate endDate) 
  {
      String api="https://api.tiingo.com/tiingo/daily/"+symbol+"/prices?startDate="+startDate+"&endDate="+endDate+
      "&token=931458d8c274ddc22ffba993b28532a37477e455";
       return api;
  }


  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades,
      LocalDate endDate) throws StockQuoteServiceException 
      {
        AnnualizedReturn annualizedRetun;

  
        List<AnnualizedReturn> annualizedReturns=new ArrayList<>();
    
        //  return annualizedReturns;
        for(int i=0;i<portfolioTrades.size();i++)
        {
          annualizedRetun=getAnnualizedReturn(portfolioTrades.get(i),endDate);
          annualizedReturns.add(annualizedRetun);
        }
        Comparator<AnnualizedReturn> sortByAnnualReturn=Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
        Collections.sort(annualizedReturns,sortByAnnualReturn);
         return annualizedReturns;

      }

//   private AnnualizedReturn getAnnualizedReturn(PortfolioTrade portfolioTrade, LocalDate endDate) throws StockQuoteServiceException 
//   {
//     AnnualizedReturn annualizedReturn;

//     String symbol=portfolioTrade.getSymbol();
//     LocalDate startLocalDate=portfolioTrade.getPurchaseDate();
// try{
//   List<Candle> candles=stockQuotesService.getStockQuote(symbol,startLocalDate,endDate);
//   Candle stockStartDate=candles.get(0);
//   Candle stockLatest=candles.get(candles.size()-1);

//   Double buyPrice=stockStartDate.getOpen();
//   Double sellPrice=stockLatest.getClose();
//   double totalreturn=(sellPrice-buyPrice)/buyPrice;
//        double total_num_years= ChronoUnit.DAYS.between(portfolioTrade.getPurchaseDate(), endDate)/365.24;

//        double annualized_returns=Math.pow((1 + totalreturn) , (1 / total_num_years) )- 1;
   
//  annualizedReturn=new AnnualizedReturn(symbol,annualized_returns,totalreturn);
//  }
    
//    catch(JsonProcessingException e){
//     annualizedReturn= new AnnualizedReturn(symbol, Double.NaN, Double.NaN);}
//     return annualizedReturn;

//   }
public AnnualizedReturn getAnnualizedReturn(PortfolioTrade trade, LocalDate endDate)
throws StockQuoteServiceException {
LocalDate startDate = trade.getPurchaseDate();
String symbol = trade.getSymbol(); 


Double buyPrice = 0.0, sellPrice = 0.0;


try {
LocalDate startLocalDate = trade.getPurchaseDate();


List<Candle> stocksStartToEndFull = stockQuotesService.getStockQuote(symbol, startLocalDate, endDate);


Collections.sort(stocksStartToEndFull, (candle1, candle2) -> { 
  return candle1.getDate().compareTo(candle2.getDate()); 
});

Candle stockStartDate = stocksStartToEndFull.get(0);
Candle stocksLatest = stocksStartToEndFull.get(stocksStartToEndFull.size() - 1);


buyPrice = stockStartDate.getOpen();
sellPrice = stocksLatest.getClose();
endDate = stocksLatest.getDate();


} catch (JsonProcessingException e) {
throw new RuntimeException();
}
Double totalReturn = (sellPrice - buyPrice) / buyPrice;


long daysBetweenPurchaseAndSelling = ChronoUnit.DAYS.between(startDate, endDate);
Double totalYears = (double) (daysBetweenPurchaseAndSelling) / 365;


Double annualizedReturn = Math.pow((1 + totalReturn), (1 / totalYears)) - 1;
return new AnnualizedReturn(symbol, annualizedReturn, totalReturn);


}

  public PortfolioManagerImpl(StockQuotesService stockQuotesService) 
  {
     this.stockQuotesService=stockQuotesService;
  }


  protected PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }








  private Comparator<AnnualizedReturn> getComparator() {
    return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  }

  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturnParallel(
      List<PortfolioTrade> portfolioTrades, LocalDate endDate, int numThreads)
      throws InterruptedException, StockQuoteServiceException,JsonProcessingException 
      {
        List<AnnualizedReturn> annualizedReturns = new ArrayList<AnnualizedReturn>();
        List<Future<AnnualizedReturn>> futureReturnsList = new ArrayList<Future<AnnualizedReturn>>();
        final ExecutorService pool = Executors.newFixedThreadPool(numThreads);
      
        for (int i = 0; i < portfolioTrades.size(); i++) 
        {
          PortfolioTrade trade = portfolioTrades.get(i);
          Callable<AnnualizedReturn> callableTask = () -> 
          {
            return getAnnualizedReturn(trade, endDate);
          };
          Future<AnnualizedReturn> futureReturns = pool.submit(callableTask);
          futureReturnsList.add(futureReturns);
        }
      
        for (int i = 0; i < portfolioTrades.size(); i++) {
          Future<AnnualizedReturn> futureReturns = futureReturnsList.get(i);
          try {
            AnnualizedReturn returns = futureReturns.get();
            annualizedReturns.add(returns); 
          } catch (ExecutionException e) {
            throw new StockQuoteServiceException("Error when calling the API",e);
      
          }
        }
        Collections.sort(annualizedReturns,getComparator());
        return annualizedReturns;
      }      
}
