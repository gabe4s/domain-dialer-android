package com.gabedouda.dig2dial;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;

public class DomainUtil extends AsyncTask<String, Void, String> {
    private OnDomainProcessed listener;

    private Exception exception;

    public DomainUtil(OnDomainProcessed listener) {
        this.listener=listener;
    }

	private ArrayList<String> getTxtRecordsForDomain(String domain) throws Exception {
		ArrayList<String> records = new ArrayList<String>();
		Lookup look;

		look = new Lookup(domain,Type.TXT);
		look.setResolver(new SimpleResolver("8.8.8.8"));
		for(Record record : look.run()){
			records.add(record.rdataToString());
		}

		return records;
	}

	public String getTnForDomain(String domain) {
		String tn = null;

		try {
            ArrayList<String> txtRecords = getTxtRecordsForDomain(domain);

            Pattern pattern = Pattern.compile("\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})");

            for(String txtRecord : txtRecords) {
                if(txtRecord.toLowerCase().contains("tn=")) {
                    Matcher matcher = pattern.matcher(txtRecord);
                    if(matcher.find()) {
                        tn = matcher.group(0);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            exception = e;
        }


		return tn;
	}

    @Override
    protected String doInBackground(String... args) {
        return getTnForDomain(args[0]);
    }

    @Override
    protected void onPostExecute(String tn) {
        listener.onDomainProcessed(tn, exception);
    }

}
