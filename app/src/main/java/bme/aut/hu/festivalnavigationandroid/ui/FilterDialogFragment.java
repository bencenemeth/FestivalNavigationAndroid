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

import java.util.ArrayList;

import bme.aut.hu.festivalnavigationandroid.R;

/**
 * Created by ben23 on 2018-03-17.
 */

public class FilterDialogFragment extends DialogFragment {

    private NoticeFilterDialogListener mCallback;

    private Context context;

    private RadioGroup rgFilterOpen;
    private RadioGroup rgFilterCategory;

    private Boolean open;
    private String category;

    // Override the Fragment.onAttach() method to instantiate the NoticeFilterDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        // Verify that the host activity implements the callback interface
        try {
            mCallback = (NoticeFilterDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeFilterDialogListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null)
            map = getArguments().getParcelable("map");*/
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogfragment_filter, null);
        builder.setView(view);

        setUpRadioGroups(view);
        setUpResponseButtons(builder);

        return builder.create();
    }

    /**
     * Setting up the RadioGroups for the filters.
     *
     * @param view  dialog's layout
     */
    private void setUpRadioGroups(View view) {
        rgFilterOpen = view.findViewById(R.id.rgFilterOpen);
        rgFilterOpen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                switch ((String) rb.getTag()) {
                    case "open":
                        open = true;
                        break;
                    case "closed":
                        open = false;
                        break;
                    default:
                        open = null;
                        break;
                }
            }
        });

        rgFilterCategory = view.findViewById(R.id.rgFilterCategory);
        rgFilterCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                category = (String) rb.getTag();
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
                mCallback.onFilterDialogPositiveClick(open, category);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    public static FilterDialogFragment newInstance(ArrayList<String> categoryNames) {
        FilterDialogFragment fragment = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("categoryNames", categoryNames);
        fragment.setArguments(args);
        return fragment;
    }

    public interface NoticeFilterDialogListener {
        public void onFilterDialogPositiveClick(Boolean open, String category);
    }
}
