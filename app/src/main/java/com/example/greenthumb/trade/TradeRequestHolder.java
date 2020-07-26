package com.example.greenthumb.trade;

import android.content.res.Resources;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.greenthumb.R;
import com.example.greenthumb.tasks.TaskTitle;

/**
 * Represents a ViewHolder for tasks.
 */
public class TradeRequestHolder extends RecyclerView.ViewHolder {
    public ImageButton options;

    private TextView title;
    private TextView dueDate;
    private TextView requester;

    Resources res = itemView.getResources();

    /**
     * Instantiates a new TaskHolder and gets references to all the views contained within it.
     * @param view
     */
    public TradeRequestHolder(View view) {
        super(view);
        this.title = view.findViewById(R.id.textViewTradeRequestTitle);
        this.dueDate = view.findViewById(R.id.textViewTradeRequestDueDate);
        this.requester = view.findViewById(R.id.textViewTradeRequester);
        this.options = view.findViewById(R.id.tradeRequestOptions);
    }

    /**
     * Sets the title field of a task in the RecyclerView.
     * @param title the title of the associated task
     * @param taskTitles an array of all task titles
     */
    public void setTitle(TaskTitle title, String[] taskTitles) {
        int titleIndex = TaskTitle.titleToInt(title);
        this.title.setText(taskTitles[titleIndex]);
    }

    /**
     * Sets the due date field of a task in the RecyclerView.
     * @param dateString the due date of the associated task
     */
    public void setDueDate(String dateString) {
        this.dueDate.setText(String.format("%s %s", res.getString(R.string.due), dateString == null ? res.getString(R.string.none) : dateString));
    }

    /**
     * Sets the assignee field of a task in the RecyclerView.
     * @param requesterLabel the email of the user who requested the trade
     */
    public void setRequester(String requesterLabel) {
        this.requester.setText(String.format("%s %s", res.getString(R.string.requested_by), requesterLabel));
    }
}
