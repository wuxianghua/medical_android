package com.palmap.exhibition.other;

import com.palmap.library.utils.LogUtil;
import com.palmap.library.utils.MapUtils;
import com.palmaplus.nagrand.data.FeatureCollection;
import com.palmaplus.nagrand.geos.Coordinate;
import com.palmaplus.nagrand.navigate.NavigateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王天明 on 2017/4/10.
 */

public class PMPDynamicNavigationManager {


    public class Line {
        public Coordinate start;
        public Coordinate end;
        public int index;
    }

    public class PMPLineGeneralEquationParameters {
        double a, b, c;
    }

    public interface CallBack {
        void call(List<Coordinate> coordinates, Line line);
    }

    private List<Coordinate> coordinates;

    public PMPDynamicNavigationManager() {
    }

    public void config(NavigateManager navigateManager, FeatureCollection featureCollection) {
        if (null == featureCollection) {
            return;
        }
        coordinates = new ArrayList<>();
        for (int i = 0; i < navigateManager.getLength(); i++) {
            coordinates.add(navigateManager.getCoordinateByFeatureCollection(featureCollection, i));
        }
    }

    public Line currentPoint(Coordinate coordinate) {
        if (coordinate == null) {
            LogUtil.e("currentPoint null !!!");
            return null;
        }
        return judgeDirection(coordinate);
    }

    private Line judgeDirection(Coordinate coordinate) {
        if (null == this.coordinates || this.coordinates.size() == 0) {
            return null;
        }
        Line line = new Line();
        double minArea = -1;
        int index = 0;
        Coordinate startP = new Coordinate(0, 0);
        Coordinate endP = new Coordinate(0, 0);
        for (int i = 0; i < this.coordinates.size(); i++) {
            if (i + 1 < this.coordinates.size()) {
                Coordinate value1 = coordinates.get(i);
                Coordinate value2 = coordinates.get(i + 1);
                double temp = trigonTotalAreaWithPoints(new Coordinate[]{coordinate,value1,value2});
                if (minArea == -1 || temp < minArea) {
                    minArea = temp;
                    startP = value1;
                    endP = value2;
                    index = i;
                }
                if (minArea == 0) {
                    index = i;
                }
                line.start = startP;
                line.end = endP;
                line.index = index;
            }
        }
        return line;
    }

    // 求三个点组成的三角形面积
    private double trigonTotalAreaWithPoints(Coordinate[] coordinates) {
        double temp = 0;
        if (coordinates.length >= 3) {
            Coordinate point0 = coordinates[0];
            Coordinate point1 = coordinates[1];
            Coordinate point2 = coordinates[2];
            double bottom = MapUtils.pointDistance(point1, point2);
            PMPLineGeneralEquationParameters linePar = getLineEquationParameterswithPoints(point1, point2);
            double height =  getDisTanceFromLine(linePar,point0);
            temp = bottom * height / 2;
        } else {
            temp = -1;
        }
        return temp;
    }


    //根据两点求直线一般式方程
    private PMPLineGeneralEquationParameters getLineEquationParameterswithPoints(Coordinate point0, Coordinate point1) {
        PMPLineGeneralEquationParameters lineParameters = new PMPLineGeneralEquationParameters();
        double a = 0, b = 0, c = 0;
        a = (point1.getX() - point0.getX());
        b = (point0.getX() - point1.getX());
        c = point1.getX() * point0.getY() - point0.getX() * point1.getY();
        lineParameters.a = a;
        lineParameters.b = b;
        lineParameters.c = c;
        return lineParameters;
    }

    //点到直线的距离
    private double getDisTanceFromLine(PMPLineGeneralEquationParameters linePar,Coordinate point) {
        return Math.abs(linePar.a * point.x + linePar.b * point.y + linePar.c)/Math.sqrt(Math.pow(linePar.a, 2) + Math.pow(linePar.b, 2));
    }

}
