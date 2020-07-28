package com.example.greenthumb.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.greenthumb.R;
import com.example.greenthumb.SignUp;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

/**
 * Represents a custom adapter for Trade Requests in the RecyclerView.
 */
public class TradeRequestAdapter extends FirebaseRecyclerAdapter<TradeRequest, TradeRequestHolder> {
    private Context context = null;

    public TradeRequestAdapter(FirebaseRecyclerOptions<TradeRequest> options) {
        super(options);
    }

    @Override
    public TradeRequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.trade_request_item, parent, false);
        final TradeRequestHolder holder = new TradeRequestHolder(view);
        return holder;
    }

    @Override
    protected void onBindViewHolder(final TradeRequestHolder holder, int position, final TradeRequest model) {
        // store associated trade request
        final TradeRequestViewModel tradeRequest = new TradeRequestViewModel(model);

        // set text on TradeRequestHolder
        holder.setTitle(model.getRequestedTask().getTitle(), context.getResources().getStringArray(R.array.task_titles));
        holder.setDueDate(model.getRequestedTask().getDateString());
        holder.setRequester(model.getRequester().getEmail());

        // set on-click methods for the options button and buttons in the options menu
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.options);
                popupMenu.getMenuInflater().inflate(R.menu.trade_request_options, popupMenu.getMenu());

                // set event handlers for menu options
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.acceptButton:
                                // accept the trade
                                tradeRequest.acceptTrade();
                                // show a toast
                                Toast.makeText(TradeRequestAdapter.this.context, R.string.trade_accepted,
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.declineButton:
                                // reject the trade
                                tradeRequest.delete();
                                Toast.makeText(TradeRequestAdapter.this.context, R.string.trade_declined,
                                        Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                return false;
                        }

                        return true;
                    }
                });

                popupMenu.show();
            }
        });
    }


}