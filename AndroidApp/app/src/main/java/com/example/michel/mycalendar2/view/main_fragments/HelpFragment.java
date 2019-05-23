package com.example.michel.mycalendar2.view.main_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.example.michel.mycalendar2.activities.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelpFragment extends Fragment {
    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    private View mView;

    private String[] mGroupsArrayQuestions;
    private String[] mGroupsArrayAnswers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = getView();
        mView=inflater.inflate(R.layout.help_fragment, container, false);

        mGroupsArrayQuestions = getResources().getStringArray(R.array.help_main_questions);
        mGroupsArrayAnswers = getResources().getStringArray(R.array.help_answers);

        Map<String, String> map;
        // коллекция для групп
        ArrayList<Map<String, String>> groupDataList = new ArrayList<>();
        // заполняем коллекцию групп из массива с названиями групп

        for (String group : mGroupsArrayQuestions) {
            // заполняем список атрибутов для каждой группы
            map = new HashMap<>();
            map.put("groupName", group); // время года
            groupDataList.add(map);
        }

        // список атрибутов групп для чтения
        String groupFrom[] = new String[] { "groupName" };
        // список ID view-элементов, в которые будет помещены атрибуты групп
        int groupTo[] = new int[] { android.R.id.text1 };

        // создаем общую коллекцию для коллекций элементов
        ArrayList<ArrayList<Map<String, String>>> сhildDataList = new ArrayList<>();

        // в итоге получится сhildDataList = ArrayList<сhildDataItemList>

        for (int i = 0; i < mGroupsArrayAnswers.length; i++){
            ArrayList<Map<String, String>> сhildDataItemList = new ArrayList<>();
            map = new HashMap<>();
            map.put("answer", mGroupsArrayAnswers[i]);
            сhildDataItemList.add(map);
            сhildDataList.add(сhildDataItemList);
        }

        // список атрибутов элементов для чтения
        String childFrom[] = new String[] { "answer" };
        // список ID view-элементов, в которые будет помещены атрибуты
        // элементов
        int childTo[] = new int[] { android.R.id.text1 };

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                mView.getContext(), groupDataList,
                android.R.layout.simple_expandable_list_item_1, groupFrom,
                groupTo, сhildDataList, android.R.layout.simple_list_item_1,
                childFrom, childTo);

        ExpandableListView expandableListView = (ExpandableListView) mView.findViewById(R.id.help_list_view);
        expandableListView.setAdapter(adapter);

        return mView;
    }
}
