package com.procter.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.procter.R;
import com.procter.interfaces.SocketDataParser;
import com.procter.model.earnings.EarningsData;
import com.procter.model.earnings.SalesReport;
import com.procter.viewmodel.CommonViewModel;
import com.procter.viewmodel.OrderViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EarningsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EarningsFragment extends BaseFragment implements SocketDataParser {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvReportYear)
    TextView tvReportYear;
    @BindView(R.id.any_chart_view)
    AnyChartView anyChartView;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;
    @BindView(R.id.txtHeader)
    TextView txtHeader;
    @BindView(R.id.tvTodayAmount)
    TextView tvTodayAmount;
    @BindView(R.id.tvCurrentDate)
    TextView tvCurrentDate;
    @BindView(R.id.pbToday)
    ProgressBar pbToday;
    @BindView(R.id.tvThisWeek)
    TextView tvThisWeek;
    @BindView(R.id.tvRangeDateAmount)
    TextView tvRangeDateAmount;
    @BindView(R.id.tvRangeDate)
    TextView tvRangeDate;
    @BindView(R.id.pbThisWeek)
    ProgressBar pbThisWeek;
    @BindView(R.id.tvThisMonth)
    TextView tvThisMonth;
    @BindView(R.id.tvThisMonthAmount)
    TextView tvThisMonthAmount;
    @BindView(R.id.tvCurrentMonth)
    TextView tvCurrentMonth;
    @BindView(R.id.pbThisMonth)
    ProgressBar pbThisMonth;
    @BindView(R.id.tvThisYear)
    TextView tvThisYear;
    @BindView(R.id.tvThisYearAmount)
    TextView tvThisYearAmount;
    @BindView(R.id.tvCurrentYear)
    TextView tvCurrentYear;
    @BindView(R.id.pbThisYear)
    ProgressBar pbThisYear;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CommonViewModel viewModel;

    public EarningsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EarningsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EarningsFragment newInstance(String param1, String param2) {
        EarningsFragment fragment = new EarningsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_earnings;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvUserName.setText(getContext().getResources().getString(R.string.hi_user, session.getUserInfo().getPharmacy_name()));
        tvReportYear.setText(getContext().getResources().getString(R.string.report_year, "2019"));
        viewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        getEarningsData();
    }

    private void getEarningsData(){
        customDialog.show();
        viewModel.getEarningsData().observe(this, earningsData -> {
            customDialog.dismiss();
            setEarningsData(earningsData);
        });
    }

    private void setEarningsData(EarningsData earningsData){
        SalesReport salesReport = earningsData.getSalesReport();
        tvTodayAmount.setText(String.valueOf(earningsData.getTodayEarnings()));
        tvThisWeek.setText(String.valueOf(earningsData.getWeeklyEarnings()));
        tvThisMonth.setText(String.valueOf(earningsData.getMonthlyEarnings()));
        tvThisYear.setText(String.valueOf(earningsData.getYearlyEarnings()));
        anyChartView.setProgressBar(progress_bar);
        anyChartView.setLicenceKey("");
        Cartesian cartesian = AnyChart.line();
        txtHeader.setText("Earnings");
        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(false);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title(false);
//        cartesian.title(getContext().getResources().getString(R.string.your_sales_report, "2019"));

//        cartesian.yAxis(0).title("Number of Bottles Sold (thousands)");
//        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("Jan", salesReport.getJsonMember1(), 2.3, 2.8));
        seriesData.add(new CustomDataEntry("Feb", salesReport.getJsonMember2(), 4.0, 4.1));
        seriesData.add(new CustomDataEntry("Mar", salesReport.getJsonMember3(), 6.2, 5.1));
        seriesData.add(new CustomDataEntry("Apr", salesReport.getJsonMember4(), 11.8, 6.5));
        seriesData.add(new CustomDataEntry("May", salesReport.getJsonMember5(), 13.0, 12.5));
        seriesData.add(new CustomDataEntry("Jun", salesReport.getJsonMember6(), 13.9, 18.0));
        seriesData.add(new CustomDataEntry("Jul", salesReport.getJsonMember7(), 18.0, 21.0));
        seriesData.add(new CustomDataEntry("Aug", salesReport.getJsonMember8(), 23.3, 20.3));
        seriesData.add(new CustomDataEntry("Sep", salesReport.getJsonMember9(), 24.7, 19.2));
        seriesData.add(new CustomDataEntry("Oct", salesReport.getJsonMember10(), 18.0, 14.4));
        seriesData.add(new CustomDataEntry("Nov", salesReport.getJsonMember11(), 15.1, 9.2));
        seriesData.add(new CustomDataEntry("Dec", salesReport.getJsonMember12(), 11.3, 5.9));

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
//        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
//        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.color("#F37474");
        series1.hovered().markers().enabled(false);
//        series1.color("#F37474");
        series1.name(getContext().getResources().getString(R.string.rs, ""));
        /*series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);*/
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(false);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
    }

    @Override
    public void parseResponse(String response) {

    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
//            setValue("value2", value2);
//            setValue("value3", value3);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
