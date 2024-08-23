package com.example.mealapp.feature.food_planner_preview.model;


import androidx.annotation.NonNull;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Set;

public class MealEventDecorator implements DayViewDecorator {

    private final Set<CalendarDay> dates;
    private final int dotColor;

    public MealEventDecorator(Set<CalendarDay> dates, int dotColor) {
        this.dates = dates;
        this.dotColor = dotColor;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(@NonNull DayViewFacade view) {
        view.addSpan(new DotSpan(dotColor));
    }
}
