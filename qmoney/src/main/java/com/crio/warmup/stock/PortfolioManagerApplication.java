

package com.crio.warmup.stock;


import com.crio.warmup.stock.dto.*;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
// import com.crio.warmup.stock.portfolio.PortfolioManager;
// import com.crio.warmup.stock.portfolio.PortfolioManagerFactory;
import com.crio.warmup.stock.portfolio.PortfolioManagerImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.crio.warmup.stock.portfolio.PortfolioManager;
import com.crio.warmup.stock.portfolio.PortfolioManagerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
//import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
//import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
//import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
//import java.util.Collections;
//import java.util.Comparator;
// import java.net.URISyntaxException;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.time.LocalDate;
// import java.time.temporal.ChronoUnit;
// import java.util.Arrays;
//import java.util.Comparator;
// import java.net.URISyntaxException;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
// import java.util.Arrays;
// import java.util.Comparator;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.ThreadContext;
//import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestTemplate;


public class PortfolioManagerApplication 
{
  public static RestTemplate restTemplate=new RestTemplate();

  public static PortfolioManager portfolioManager=PortfolioManagerFactory.getPortfolioManager(restTemplate);

  public static List<String> mainReadFile(String[] args) throws IOException, URISyntaxException
   {
   // String file=args[0];

  //  String contents=readFileAsString(file);

  File file=resolveFileFromResources(args[0]);
    ObjectMapper objectMapper = getObjectMapper();
    PortfolioTrade[] portfolioTrade =objectMapper.readValue(file,PortfolioTrade[].class);
  //   return Collections.emptyList();
  List<String> list=new ArrayList<>();
  for(int i=0;i<portfolioTrade.length;i++)
  {
    list.add(portfolioTrade[i].getSymbol());
  }
  return Stream.of(portfolioTrade).map(PortfolioTrade::getSymbol).collect(Collectors.toList());

  
  //return Collections.emptyList();
  }





  private static void printJsonObject(Object object) throws IOException 
  {
    Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
    ObjectMapper mapper = new ObjectMapper();
    logger.info(mapper.writeValueAsString(object));
  }

  private static File resolveFileFromResources(String filename) throws URISyntaxException
   {
    return Paths.get(
        Thread.currentThread().getContextClassLoader().getResource(filename).toURI()).toFile();
  }
private static String readFileAsString(String filename) throws UnsupportedEncodingException, IOException, URISyntaxException
{
  return new String(Files.readAllBytes(resolveFileFromResources(filename).toPath()),"UTF-8");
}
  private static ObjectMapper getObjectMapper() 
  {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }


  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Follow the instructions provided in the task documentation and fill up the correct values for
  //  the variables provided. First value is provided for your reference.
  //  A. Put a breakpoint on the first line inside mainReadFile() which says
  //    return Collections.emptyList();
  //  B. Then Debug the test #mainReadFile provided in PortfoliomanagerApplicationTest.java
  //  following the instructions to run the test.
  //  Once you are able to run the test, perform following tasks and record the output as a
  //  String in the function below.
  //  Use this link to see how to evaluate expressions -
  //  https://code.visualstudio.com/docs/editor/debugging#_data-inspection
  //  1. evaluate the value of "args[0]" and set the value
  //     to the variable named valueOfArgument0 (This is implemented for your reference.)
  //  2. In the same window, evaluate the value of expression below and set it
  //  to resultOfResolveFilePathArgs0
  //     expression ==> resolveFileFromResources(args[0])
  //  3. In the same window, evaluate the value of expression below and set it
  //  to toStringOfObjectMapper.
  //  You might see some garbage numbers in the output. Dont worry, its expected.
  //    expression ==> getObjectMapper().toString()
  //  4. Now Go to the debug window and open stack trace. Put the name of the function you see at
  //  second place from top to variable functionNameFromTestFileInStackTrace
  //  5. In the same window, you will see the line number of the function in the stack trace window.
  //  assign the same to lineNumberFromTestFileInStackTrace
  //  Once you are done with above, just run the corresponding test and
  //  make sure its working as expected. use below command to do the same.
  //  ./gradlew test --tests PortfolioManagerApplicationTest.testDebugValues

  public static List<String> debugOutputs()
   {

     String valueOfArgument0 = "trades.json";
     String resultOfResolveFilePathArgs0 = "trades.json";
     String toStringOfObjectMapper = "ObjectMapper";
     String functionNameFromTestFileInStackTrace = "mainReadFile";
     String lineNumberFromTestFileInStackTrace = "mainReadFile";


    return Arrays.asList(new String[]
    {valueOfArgument0, resultOfResolveFilePathArgs0,
        toStringOfObjectMapper, functionNameFromTestFileInStackTrace,
        lineNumberFromTestFileInStackTrace});
  }

public static List<TotalReturnsDto> mainReadQuotesHelper(String args[],List<PortfolioTrade> trades) throws  IOException, URISyntaxException 
{
  RestTemplate restTemplate=new RestTemplate();
  List<TotalReturnsDto> tests=new ArrayList<>();
  for(PortfolioTrade t:trades)
  {
    String uri="https://api.tiingo.com/tiingo/daily/"+t.getSymbol()+"/prices?startDate="+t.getPurchaseDate()+"&endDate="+args[1]+
    "&token=931458d8c274ddc22ffba993b28532a37477e455";
    TiingoCandle[] results=restTemplate.getForObject(uri, TiingoCandle[].class);
    if(results!=null)
    {
      tests.add(new TotalReturnsDto(t.getSymbol(), results[results.length-1].getClose()));
    }
  }
  return tests;
}

  // Note:
  // Remember to confirm that you are getting same results for annualized returns as in Module 3.
  public static List<String> mainReadQuotes(String[] args) throws IOException, URISyntaxException 
  {
  //  File file=resolveFileFromResources(args[0]);
    ObjectMapper objectMapper = getObjectMapper();
    List<PortfolioTrade> trades=Arrays.asList(objectMapper.readValue(resolveFileFromResources(args[0]),PortfolioTrade[].class));
 //   PortfolioTrade[] portfolioTrade =objectMapper.readValue(file,PortfolioTrade[].class);
  //   return Collections.emptyList();
  List<TotalReturnsDto> sortedByValue=mainReadQuotesHelper(args, trades);
  Collections.sort(sortedByValue,TotalReturnsDto.closingComaparator);
  List<String> stocks=new ArrayList<>();  
  for(TotalReturnsDto trd:sortedByValue)
  {
    stocks.add(trd.getSymbol());
  }
 
     return stocks;
  }

  // // // TODO:
  // // //  After refactor, make sure that the tests pass by using these two commands
  // // //  ./gradlew test --tests PortfolioManagerApplicationTest.readTradesFromJson
  // // //  ./gradlew test --tests PortfolioManagerApplicationTest.mainReadFile
  public static List<PortfolioTrade> readTradesFromJson(String filename) throws IOException, URISyntaxException 
  {
    File file=resolveFileFromResources(filename);
    ObjectMapper objectMapper = getObjectMapper();
    PortfolioTrade[] portfolioTrade =objectMapper.readValue(file,PortfolioTrade[].class);

    List<PortfolioTrade> tests=Arrays.asList(portfolioTrade);

   
    return tests;
  }

  public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) 
  {
    String api="https://api.tiingo.com/tiingo/daily/"+trade.getSymbol()+"/prices?startDate="+trade.getPurchaseDate()+"&endDate="+endDate+
    "&token="+token;
    //    String symbol=trade.getSymbol();
    //  PortfolioManagerImpl p=new PortfolioManagerImpl();
    // String api=p.buildUri(trade.getSymbol(),endDate,trade.getPurchaseDate());
     return api;
  }
  // TODO:
  //  Ensure all tests are passing using below command
  //  ./gradlew test --tests ModuleThreeRefactorTest
  static Double getOpeningPriceOnStartDate(List<Candle> candles) {
     return candles.get(0).getOpen();
  }


  public static Double getClosingPriceOnEndDate(List<Candle> candles) {
    return candles.get(candles.size()-1).getClose();
  }
  public static String getToken() {
    return "931458d8c274ddc22ffba993b28532a37477e455";
  }

  public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) throws JsonProcessingException
  {
    String uri=prepareUrl(trade,endDate,token);
    RestTemplate restTemplate=new RestTemplate();
    Candle[] candles=restTemplate.getForObject(uri, TiingoCandle[].class);
   // PortfolioManagerImpl p=new PortfolioManagerImpl();
  // List<Candle> res= p.getStockQuote(trade.getSymbol(), trade.getPurchaseDate(), endDate);
    return Arrays.asList(candles);
 
  }

  public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args)
      throws IOException, URISyntaxException 
  {
    List<AnnualizedReturn> annualizedReturns=new ArrayList<>();
    List<PortfolioTrade> portfolioTrades=readTradesFromJson(args[0]);
    for(PortfolioTrade pt:portfolioTrades)
    {
    List<Candle> candles=fetchCandles(pt,LocalDate.parse(args[1]),getToken());
     annualizedReturns.add(calculateAnnualizedReturns(LocalDate.parse(args[1]),pt,getOpeningPriceOnStartDate(candles),getClosingPriceOnEndDate(candles)));
    }
    Comparator<AnnualizedReturn> sortByAnnualReturn=Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
    Collections.sort(annualizedReturns,sortByAnnualReturn);
   // PortfolioManagerImpl p=new PortfolioManagerImpl();
    // List<AnnualizedReturn> res =p.calculateAnnualizedReturn(readTradesFromJson(args[0]),LocalDate.parse(args[1]));
      return annualizedReturns;
  }

  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Return the populated list of AnnualizedReturn for all stocks.
  //  Annualized returns should be calculated in two steps:
  //   1. Calculate totalReturn = (sell_value - buy_value) / buy_value.
  //      1.1 Store the same as totalReturns
  //   2. Calculate extrapolated annualized returns by scaling the same in years span.
  //      The formula is:
  //      annualized_returns = (1 + total_returns) ^ (1 / total_num_years) - 1
  //      2.1 Store the same as annualized_returns
  //  Test the same using below specified command. The build should be successful.
  //     ./gradlew test --tests PortfolioManagerApplicationTest.testCalculateAnnualizedReturn

  public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
      PortfolioTrade trade, Double buyPrice, Double sellPrice) 
      {
        // PortfolioManagerImpl p=new PortfolioManagerImpl();
        // Annualized calculateAnnualizedReturn
        double totalreturn=(sellPrice-buyPrice)/buyPrice;
        double total_num_years= ChronoUnit.DAYS.between(trade.getPurchaseDate(), endDate)/365.24;

        double annualized_returns=Math.pow((1 + totalreturn) , (1 / total_num_years) )- 1;

            return new AnnualizedReturn(trade.getSymbol(), annualized_returns, totalreturn);
      }
//import org.springframework.web.client.RestTemplate;

    }
//public class PortfolioManagerApplication {
// import org.springframework.web.client.RestTemplate;


// public class PortfolioManagerApplication {



























//   // TODO: CRIO_TASK_MODULE_REFACTOR
//   //  Once you are done with the implementation inside PortfolioManagerImpl and
//   //  PortfolioManagerFactory, create PortfolioManager using PortfolioManagerFactory.
//   //  Refer to the code from previous modules to get the List<PortfolioTrades> and endDate, and
//   //  call the newly implemented method in PortfolioManager to calculate the annualized returns.

//   // Note:
//   // Remember to confirm that you are getting same results for annualized returns as in Module 3.

//   public static List<AnnualizedReturn> mainCalculateReturnsAfterRefactor(String[] args)
//       throws Exception {
      
//       PortfolioManagerImpl p=new PortfolioManagerImpl(restTemplate);
//       List<AnnualizedReturn> res =p.calculateAnnualizedReturn(readTradesFromJson(args[0]),LocalDate.parse(args[1]));
//        return res;
//   }


//   public static void main(String[] args) throws Exception 
//   {
//     Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
//     ThreadContext.put("runId", UUID.randomUUID().toString());

//     printJsonObject(mainReadFile(args));


//     printJsonObject(mainReadQuotes(args));



//     printJsonObject(mainCalculateSingleReturn(args));

//   }





  



//  //   printJsonObject(mainCalculateReturnsAfterRefactor(args));
  


















//   // public static void main(String[] args) throws Exception {
//   //   Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
//   //   ThreadContext.put("runId", UUID.randomUUID().toString());
//   // }
// }


//   // public static void main(String[] args) throws Exception {
//   //   Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
//   //   ThreadContext.put("runId", UUID.randomUUID().toString());




//   // }


//   // public static void main(String[] args) throws Exception {
//   //   Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
//   //   ThreadContext.put("runId", UUID.randomUUID().toString());




//   // }

//   // public static void main(String[] args) throws Exception {
//   //   Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
//   //   ThreadContext.put("runId", UUID.randomUUID().toString());




//   // }
// }

