package com.sidia.fabio.bakingrecipe.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.sidia.fabio.bakingrecipe.RecipeViewFactory;

public class RecipeWidgetService extends RemoteViewsService {
    public RecipeWidgetService() {
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new RecipeViewFactory(this.getApplicationContext(),
                intent));
    }
}
