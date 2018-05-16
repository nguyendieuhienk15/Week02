package com.example.android.xyztouristattractions.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class FloatingActionMenu {
    private boolean isOpened;
    private FloatingActionButton menu;
    private List<FloatingActionButton> actions = new List<FloatingActionButton>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<FloatingActionButton> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] a) {
            return null;
        }

        @Override
        public boolean add(FloatingActionButton floatingActionButton) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends FloatingActionButton> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, @NonNull Collection<? extends FloatingActionButton> c) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public FloatingActionButton get(int index) {
            return null;
        }

        @Override
        public FloatingActionButton set(int index, FloatingActionButton element) {
            return null;
        }

        @Override
        public void add(int index, FloatingActionButton element) {

        }

        @Override
        public FloatingActionButton remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @NonNull
        @Override
        public ListIterator<FloatingActionButton> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<FloatingActionButton> listIterator(int index) {
            return null;
        }

        @NonNull
        @Override
        public List<FloatingActionButton> subList(int fromIndex, int toIndex) {
            return null;
        }
    };

    public FloatingActionMenu(FloatingActionButton btn){
        menu = btn;
        isOpened = false;
    }

    public void addAction(FloatingActionButton btn) {
        actions.add(btn);
    }

    public void showMenu(){
        isOpened = true;

        for (FloatingActionButton btn : actions) {
            btn.animate().translationY(10);
        }
    }

    public void closeMenu(){
        isOpened = false;

        for (FloatingActionButton btn : actions) {
            btn.animate().translationY(0);
        }
    }

    public void menuClicked () {
        if(!isOpened){
            showMenu();
        }else{
            closeMenu();
        }
    }

    public FloatingActionButton getMenu() {
        return menu;
    }
}
