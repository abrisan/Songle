package datastructures;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import tools.PrettyPrinter;

public class TradeDescriptor
{
    public String fromClass;
    public String toClass;
    public int fromAmount;
    public int toAmount;

    public static class ActualTrade implements PrettyPrinter
    {
        public int from;
        public int to;
        public String toClass;

        public String serialise() throws JSONException
        {
            JSONObject obj = new JSONObject();

            obj . put("fromQty", from);
            obj . put("toQty", to);
            obj . put("toClass", toClass);

            return obj.toString();
        }

        public static ActualTrade deserialise(String jsonString)
                throws JSONException
        {
            JSONObject jsonObject = new JSONObject(jsonString);
            ActualTrade ret_value = new ActualTrade();

            ret_value . from = jsonObject.getInt("fromQty");
            ret_value . to = jsonObject.getInt("toQty");
            ret_value . toClass = jsonObject.getString("toClass");

            return ret_value;
        }
    }

    public TradeDescriptor() {}
    public TradeDescriptor(String from, String to, int fromAmount, int toAmount)
    {
        this . fromClass = from;
        this . toClass = to;

        this . fromAmount = fromAmount;
        this . toAmount = toAmount;
    }

    public ActualTrade getRate(String from, int amount)
    {
        ActualTrade ret_value = null;
        if (from.equals(this.fromClass))
        {
            if (amount < this . fromAmount)
            {
                return null;
            }
            ret_value = new ActualTrade();
            ret_value . from = amount;
            ret_value . to = (int) Math.ceil(amount / (double) this . fromAmount);
            ret_value . toClass = this . toClass;
        }
        else if (from.equals(this . toClass))
        {
            ret_value = new ActualTrade();
            ret_value . from = amount;
            ret_value . to = amount * this . fromAmount;
            ret_value . toClass = this . fromClass;
        }
        return ret_value;
    }
}
