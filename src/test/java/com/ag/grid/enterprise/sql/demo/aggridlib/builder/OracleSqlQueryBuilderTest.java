package com.ag.grid.enterprise.sql.demo.aggridlib.builder;

import com.ag.grid.enterprise.sql.demo.aggridlib.request.ColumnVO;
import com.ag.grid.enterprise.sql.demo.aggridlib.request.EnterpriseGetRowsRequest;
import com.ag.grid.enterprise.sql.demo.aggridlib.request.SortModel;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class OracleSqlQueryBuilderTest {

    @Test
    public void singleGroup() {
        EnterpriseGetRowsRequest request = new EnterpriseGetRowsRequest();
        request.setStartRow(0);
        request.setEndRow(100);
        request.setRowGroupCols(singletonList(
                new ColumnVO("COUNTRY", "Country", "COUNTRY", "")
        ));
        request.setValueCols(asList(
                new ColumnVO("GOLD", "Gold", "GOLD", "sum"),
                new ColumnVO("SILVER", "Silver", "SILVER", "sum"),
                new ColumnVO("BRONZE", "Bronze", "BRONZE", "sum"),
                new ColumnVO("TOTAL", "Total", "TOTAL", "sum")
        ));

        String sql = new OracleSqlQueryBuilder().createSql(request, "medal", emptyMap());

        assertEquals("SELECT COUNTRY, sum(GOLD) as GOLD, sum(SILVER) as SILVER, sum(BRONZE) as BRONZE, " +
                "sum(TOTAL) as TOTAL FROM medal GROUP BY COUNTRY OFFSET 0 ROWS FETCH NEXT 101 ROWS ONLY", sql);
    }

    @Test
    public void multipleGroups() {
        EnterpriseGetRowsRequest request = new EnterpriseGetRowsRequest();
        request.setStartRow(100);
        request.setEndRow(200);
        request.setRowGroupCols(asList(
                new ColumnVO("COUNTRY", "Country", "COUNTRY", ""),
                new ColumnVO("YEAR", "Year", "YEAR", "")
        ));
        request.setValueCols(asList(
                new ColumnVO("GOLD", "Gold", "GOLD", "sum"),
                new ColumnVO("SILVER", "Silver", "SILVER", "sum"),
                new ColumnVO("BRONZE", "Bronze", "BRONZE", "sum"),
                new ColumnVO("TOTAL", "Total", "TOTAL", "sum")
        ));

        String sql = new OracleSqlQueryBuilder().createSql(request, "medal", emptyMap());

        assertEquals("SELECT COUNTRY, sum(GOLD) as GOLD, sum(SILVER) as SILVER, sum(BRONZE) as BRONZE, " +
                "sum(TOTAL) as TOTAL FROM medal GROUP BY COUNTRY OFFSET 100 ROWS FETCH NEXT 101 ROWS ONLY", sql);
    }

    @Test
    public void twoGroupsWithGroupKey() {
        EnterpriseGetRowsRequest request = new EnterpriseGetRowsRequest();
        request.setStartRow(100);
        request.setEndRow(200);
        request.setRowGroupCols(asList(
                new ColumnVO("COUNTRY", "Country", "COUNTRY", ""),
                new ColumnVO("YEAR", "Year", "YEAR", "")
        ));
        request.setValueCols(asList(
                new ColumnVO("GOLD", "Gold", "GOLD", "sum"),
                new ColumnVO("SILVER", "Silver", "SILVER", "sum"),
                new ColumnVO("BRONZE", "Bronze", "BRONZE", "sum"),
                new ColumnVO("TOTAL", "Total", "TOTAL", "sum")
        ));
        request.setGroupKeys(singletonList("Denmark"));

        String sql = new OracleSqlQueryBuilder().createSql(request, "medal", emptyMap());

        assertEquals("SELECT COUNTRY, YEAR, sum(GOLD) as GOLD, sum(SILVER) as SILVER, sum(BRONZE) as BRONZE, " +
                "sum(TOTAL) as TOTAL FROM medal WHERE COUNTRY = 'Denmark' GROUP BY COUNTRY, YEAR " +
                "OFFSET 100 ROWS FETCH NEXT 101 ROWS ONLY", sql);
    }

    @Test
    public void singleGroupWithFilteringAndSorting() {
        EnterpriseGetRowsRequest request = new EnterpriseGetRowsRequest();
        request.setStartRow(0);
        request.setEndRow(100);
        request.setRowGroupCols(singletonList(
                new ColumnVO("COUNTRY", "Country", "COUNTRY", "")
        ));
        request.setValueCols(asList(
                new ColumnVO("GOLD", "Gold", "GOLD", "sum"),
                new ColumnVO("SILVER", "Silver", "SILVER", "sum"),
                new ColumnVO("BRONZE", "Bronze", "BRONZE", "sum"),
                new ColumnVO("TOTAL", "Total", "TOTAL", "sum")
        ));


//        FilterModel1<SetFilter> setFilter = new FilterModel1<>();
//        FilterModel1<NumberFilter> numberFilter = new FilterModel1<>();
//
//        Map<String, FilterModel1> columnFilters = new HashMap<>();
//        columnFilters.put("age", numberFilter);
//        columnFilters.put("sport", setFilter);
//
//        columnFilters.entrySet().forEach(entry -> {
//            Filter filter = entry.getValue().getFilter();
//            System.out.println(filter);
//        });
//







//        HashMap<String, FilterModel1> filterModel = new HashMap<>();
//        filterModel.put("COUNTRY", new FilterModel1());

//        FilterModel1<SetFilter> fm = new FilterModel1<>("set", );


//        request.setFilterModel(new HashMap<String, FilterModel>() {{
//            put("SPORT", new FilterModel(null, null, null, "set", asList("Rowing", "Tennis")));
//            put("AGE", new FilterModel("equals", "22", null, "number", null));
//        }});
        request.setSortModel(singletonList(new SortModel("ATHLETE", "asc")));

        String sql = new OracleSqlQueryBuilder().createSql(request, "medal", emptyMap());

        assertEquals("SELECT COUNTRY, sum(GOLD) as GOLD, sum(SILVER) as SILVER, sum(BRONZE) as BRONZE, " +
                "sum(TOTAL) as TOTAL FROM medal WHERE SPORT IN ('Rowing', 'Tennis') AND AGE = 22 GROUP BY COUNTRY " +
                "OFFSET 0 ROWS FETCH NEXT 101 ROWS ONLY", sql);
    }

    @Test
    public void pivotModeNoPivotCols() {
        EnterpriseGetRowsRequest request = new EnterpriseGetRowsRequest();
        request.setStartRow(0);
        request.setEndRow(100);
        request.setRowGroupCols(singletonList(
                new ColumnVO("COUNTRY", "Country", "COUNTRY", "")
        ));
        request.setValueCols(asList(
                new ColumnVO("GOLD", "Gold", "GOLD", "sum"),
                new ColumnVO("SILVER", "Silver", "SILVER", "sum"),
                new ColumnVO("BRONZE", "Bronze", "BRONZE", "sum"),
                new ColumnVO("TOTAL", "Total", "TOTAL", "sum")
        ));
        request.setPivotMode(true);

        String sql = new OracleSqlQueryBuilder().createSql(request, "medal", emptyMap());

        assertEquals("SELECT COUNTRY, sum(GOLD) as GOLD, sum(SILVER) as SILVER, sum(BRONZE) as BRONZE, " +
                "sum(TOTAL) as TOTAL FROM medal GROUP BY COUNTRY OFFSET 0 ROWS FETCH NEXT 101 ROWS ONLY", sql);
    }

    @Test
    public void pivotModeWithSinglePivotCol() {
        EnterpriseGetRowsRequest request = new EnterpriseGetRowsRequest();
        request.setStartRow(0);
        request.setEndRow(100);
        request.setRowGroupCols(singletonList(
                new ColumnVO("COUNTRY", "Country", "COUNTRY", "")
        ));
        request.setValueCols(asList(
                new ColumnVO("GOLD", "Gold", "GOLD", "sum"),
                new ColumnVO("SILVER", "Silver", "SILVER", "sum"),
                new ColumnVO("BRONZE", "Bronze", "BRONZE", "sum"),
                new ColumnVO("TOTAL", "Total", "TOTAL", "sum")
        ));
        request.setPivotMode(true);
        request.setPivotCols(singletonList(
                new ColumnVO("SPORT", "Sport", "SPORT", null)
        ));

        Map<String, List<String>> pivotValues = new HashMap<>();
        pivotValues.put("SPORT", asList("Athletics", "Speed Skating"));
        pivotValues.put("YEAR", asList("2000", "2004"));

        String sql = new OracleSqlQueryBuilder().createSql(request, "medal", pivotValues);

        assertEquals("SELECT COUNTRY, sum(DECODE(SPORT, 'Athletics', DECODE(YEAR, '2000', GOLD))) \"Athletics_2000_GOLD\"," +
                " sum(DECODE(SPORT, 'Athletics', DECODE(YEAR, '2000', SILVER))) \"Athletics_2000_SILVER\", sum(DECODE(SPORT," +
                " 'Athletics', DECODE(YEAR, '2000', BRONZE))) \"Athletics_2000_BRONZE\", sum(DECODE(SPORT, 'Athletics'," +
                " DECODE(YEAR, '2000', TOTAL))) \"Athletics_2000_TOTAL\", sum(DECODE(SPORT, 'Athletics', DECODE(YEAR," +
                " '2004', GOLD))) \"Athletics_2004_GOLD\", sum(DECODE(SPORT, 'Athletics', DECODE(YEAR, '2004', SILVER)))" +
                " \"Athletics_2004_SILVER\", sum(DECODE(SPORT, 'Athletics', DECODE(YEAR, '2004', BRONZE)))" +
                " \"Athletics_2004_BRONZE\", sum(DECODE(SPORT, 'Athletics', DECODE(YEAR, '2004', TOTAL))) \"Athletics_2004_TOTAL\"," +
                " sum(DECODE(SPORT, 'Speed Skating', DECODE(YEAR, '2000', GOLD))) \"Speed Skating_2000_GOLD\", sum(DECODE(SPORT," +
                " 'Speed Skating', DECODE(YEAR, '2000', SILVER))) \"Speed Skating_2000_SILVER\", sum(DECODE(SPORT, 'Speed Skating'," +
                " DECODE(YEAR, '2000', BRONZE))) \"Speed Skating_2000_BRONZE\", sum(DECODE(SPORT, 'Speed Skating', DECODE(YEAR," +
                " '2000', TOTAL))) \"Speed Skating_2000_TOTAL\", sum(DECODE(SPORT, 'Speed Skating', DECODE(YEAR, '2004', GOLD)))" +
                " \"Speed Skating_2004_GOLD\", sum(DECODE(SPORT, 'Speed Skating', DECODE(YEAR, '2004', SILVER))) " +
                "\"Speed Skating_2004_SILVER\", sum(DECODE(SPORT, 'Speed Skating', DECODE(YEAR, '2004', BRONZE))) " +
                "\"Speed Skating_2004_BRONZE\", sum(DECODE(SPORT, 'Speed Skating', DECODE(YEAR, '2004', TOTAL))) " +
                "\"Speed Skating_2004_TOTAL\" FROM medal GROUP BY COUNTRY OFFSET 0 ROWS FETCH NEXT 101 ROWS ONLY", sql);
    }

    @Test
    public void fred() {

//        Filter setFilter = new SetFilter(Arrays.asList("Athletics", "Tennis"));
//        Filter numberFilter = new NumberFilter();
//
//        // fm.getType()
//
//        Map<String, Filter> columnFilters = new HashMap<>();
//        columnFilters.put("age", numberFilter);
//        columnFilters.put("sport", setFilter);
//
//        columnFilters.forEach((key, value) -> {
//            System.out.println(value);
//        });

    }

}