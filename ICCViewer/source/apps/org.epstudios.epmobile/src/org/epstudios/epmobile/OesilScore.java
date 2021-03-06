package org.epstudios.epmobile;

import android.view.View;
import android.widget.CheckBox;

public class OesilScore extends SyncopeRiskScore {
	// These scores are from the validation group in the paper.
	// The derivation cohort had slightly different scores.
	final double risk[] = new double[] { 0, 0.6, 14, 29, 52.9 };

	@Override
	protected void setContentView() {
		setContentView(R.layout.simplerisk);
	}

	@Override
	protected void calculateResult() {
		int result = 0;
		for (int i = 0; i < checkBox.length; i++) {
			if (checkBox[i].isChecked()) {
				result++;
			}
		}
		displayResult(getResultMessage(result),
				getString(R.string.syncope_oesil_score_title));
	}

	private String getResultMessage(int result) {
		String message;

		message = "OESIL Score = " + result + "\n"
				+ "1-year total mortality = " + risk[result] + "%\nReference: "
				+ getString(R.string.oesil_score_reference);
		return message;

	}

	@Override
	protected void init() {
		checkBox = new CheckBox[6];

		checkBox[0] = (CheckBox) findViewById(R.id.risk_one);
		checkBox[1] = (CheckBox) findViewById(R.id.risk_two);
		checkBox[2] = (CheckBox) findViewById(R.id.risk_three);
		checkBox[3] = (CheckBox) findViewById(R.id.risk_four);
		checkBox[4] = (CheckBox) findViewById(R.id.risk_five);
		checkBox[5] = (CheckBox) findViewById(R.id.risk_six);

		checkBox[4].setVisibility(View.GONE);
		checkBox[5].setVisibility(View.GONE);

		checkBox[0].setText(getString(R.string.abnormal_ecg_label));
		checkBox[1].setText(getString(R.string.history_cv_disease_label));
		checkBox[2].setText(getString(R.string.lack_of_prodrome_label));
		checkBox[3].setText(getString(R.string.age_over_65_label));
	}

}
