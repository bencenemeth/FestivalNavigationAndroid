package bme.aut.hu.festivalnavigationandroid.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import bme.aut.hu.festivalnavigationandroid.R;

/**
 * Created by ben23 on 2018-03-17.
 */

public class SortDialogFragment extends DialogFragment {

    private NoticeSortDialogListener mCallback;

    private Context context;

    private RadioGroup rgSort;

    private int sortMode = -1;

    // Override the Fragment.onAttach() method to instantiate the NoticeFilterDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        // Verify that the host activity implements the callback interface
        try {
            mCallback = (NoticeSortDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeFilterDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogfragment_sort, null);
        builder.setView(view);

        setUpRadioGroup(view);
        setUpResponseButtons(builder);

        return builder.create();
    }

    /**
     * Setting up the RadioGroups for the filters.
     *
     * @param view  dialog's layout
     */
    private void setUpRadioGroup(View view) {
        rgSort = view.findViewById(R.id.rgSort);
        rgSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb.getTag().equals("alpha"))
                    sortMode = 0;
                else if (rb.getTag().equals("distance"))
                    sortMode = 1;
            }
        });
    }

    /**
     * Setting up the response buttons for the dialog.
     *
     * @param builder   dialog's builder
     */
    private void setUpResponseButtons(AlertDialog.Builder builder) {
        // Add action buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCallback.onSortDialogPositiveClick(sortMode);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    public interface NoticeSortDialogListener {
        public void onSortDialogPositiveClick(int sortMode);
    }
}
