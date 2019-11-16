package com.SoftwareMatrix;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/*
    Singleton class that manages update events
 */
public class UpdateManager {
    private static UpdateManager instance = null;
    private Project project;
    private List<UpdateObserver> mObservers; // method observer list
    private List<UpdateObserver> cObservers; // class observer list
    private List<UpdateObserver> pObservers; // package observer list

    private void notifyObservers(PsiTreeChangeEvent event) {
        for(PsiElement elem : List.of(event.getChild(), event.getParent())) {
            PsiPackage pElem = PsiTreeUtil.getParentOfType(elem, PsiPackage.class, false);
            PsiClass cElem = PsiTreeUtil.getParentOfType(elem, PsiClass.class, false);
            PsiMethod mElem = PsiTreeUtil.getParentOfType(elem, PsiMethod.class, false);

            if(cElem != null) {
                if(pElem == null)
                    pElem = JavaDirectoryService.getInstance().getPackage(
                            cElem.getContainingFile().getContainingDirectory());
                for(UpdateObserver observer: cObservers) {
                    observer.update(project, cElem);
                }
            }
            if(mElem != null) {
                for(UpdateObserver observer: mObservers) {
                    observer.update(project, mElem);
                }
            }
            if(elem instanceof PsiDirectory && pElem == null) {
                pElem = JavaDirectoryService.getInstance().getPackage((PsiDirectory) elem);
            }

            if(pElem != null) {
                for(UpdateObserver observer: pObservers) {
                    observer.update(project, pElem);
                }
            }
        }
    }

    private UpdateManager(@NotNull Project project) {
        mObservers = new ArrayList<>();
        cObservers = new ArrayList<>();
        pObservers = new ArrayList<>();

        this.project = project;
        PsiManager.getInstance(project).addPsiTreeChangeListener(new PsiTreeChangeAdapter() {
            @Override
            public void childAdded(@NotNull PsiTreeChangeEvent event) {
                notifyObservers(event);
            }

            @Override
            public void childRemoved(@NotNull PsiTreeChangeEvent event) {
                notifyObservers(event);
            }

            @Override
            public void childReplaced(@NotNull PsiTreeChangeEvent event) {
                notifyObservers(event);
            }
        });
    }

    public static UpdateManager getInstance(@NotNull Project project) {
        if(instance == null) {
            instance = new UpdateManager(project);
        }
        return instance;
    }

    public boolean addMethodObserver(UpdateObserver observer) {
        return mObservers.add(observer);
    }

    public boolean removeMethodObserver(UpdateObserver observer) {
        return mObservers.remove(observer);
    }

    public boolean addClassObserver(UpdateObserver observer) {
        return cObservers.add(observer);
    }

    public boolean removeClassObserver(UpdateObserver observer) {
        return cObservers.remove(observer);
    }

    public boolean addPackageObserver(UpdateObserver observer) {
        return pObservers.add(observer);
    }

    public boolean removePackageObserver(UpdateObserver observer) {
        return pObservers.remove(observer);
    }
}
