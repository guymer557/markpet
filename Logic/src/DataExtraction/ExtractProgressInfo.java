package DataExtraction;

/**
 * Created by Guy on 30/08/2017.
 */
public class ExtractProgressInfo {
    private static double m_TotalPercentsOfRunningTask = 0;
    private final double m_TotalDividedToPercents;
    private double m_LastPercentAdded = 0;


    public void UpdateTotalPercentsOfRunningTask(double i_NewTotalPercents){
        double possiblePercentToAdd = i_NewTotalPercents - m_LastPercentAdded;
        if(possiblePercentToAdd != 0) {
            m_TotalPercentsOfRunningTask = (m_TotalPercentsOfRunningTask + possiblePercentToAdd);
            m_LastPercentAdded = i_NewTotalPercents;
        }
        if((int)i_NewTotalPercents == m_TotalDividedToPercents){
            m_LastPercentAdded = 0;
        }

    }

    static public double GetTotalPercents(){
        return m_TotalPercentsOfRunningTask;
    }

    static public void ResetProgressCounter(){
        m_TotalPercentsOfRunningTask = 0;
    }

    public void RoundUpTotalPercentsOfRunningTask(){
       m_TotalPercentsOfRunningTask = 100;
    }

    public void CalculateAndUpdate(double i_CurrentTotal, double i_CurrentPart){
        double percentage = (i_CurrentPart / i_CurrentTotal) * 100;
        double percentOfTheTotalPartPercent = (percentage * m_TotalDividedToPercents) / 100;
        UpdateTotalPercentsOfRunningTask(percentOfTheTotalPartPercent);
    }

    public ExtractProgressInfo(int i_Total){
        this.m_TotalDividedToPercents = 100 / i_Total;
    }
}
