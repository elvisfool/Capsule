package data.matrix;

import data.Tuple;
import util.MapUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ArrayMatrix<R, C, V> implements Matrix<R, C, V> {
    
    private final LinkedList<R>             rows;
    private final LinkedList<C>             cols;
    private final LinkedHashMap<R, Integer> rowIdxes;
    private final LinkedHashMap<C, Integer> colIdxes;
    private final V[][]                     data;
    
    public ArrayMatrix(Iterable<R> iterableRows, Iterable<C> iterableCols) {
        Objects.requireNonNull(iterableRows);
        Objects.requireNonNull(iterableCols);
        rows = StreamSupport.stream(iterableRows.spliterator(), false)
                            .collect(Collectors.toCollection(LinkedList::new));
        cols = StreamSupport.stream(iterableCols.spliterator(), false)
                            .collect(Collectors.toCollection(LinkedList::new));
        
        rowIdxes = MapUtil.mapIndex(iterableRows, LinkedHashMap::new);
        colIdxes = MapUtil.mapIndex(iterableCols, LinkedHashMap::new);
        
        @SuppressWarnings("unchecked")
        V[][] tmp = (V[][]) new Object[rows.size()][cols.size()];
        data = tmp;
    }
    
    public ArrayMatrix<R, C, V> slice(Tuple<Integer, Integer> rowIdxRange, Tuple<Integer, Integer> colIdxRange) {
        
        Integer rowStartIdx = rowIdxRange.getX();
        Integer rowEndIdx = rowIdxRange.getY();
        Integer colStartIdx = colIdxRange.getX();
        Integer colEndIdx = colIdxRange.getY();
        
        LinkedList<R> newRowList = new LinkedList<>(rows.subList(rowStartIdx, rowEndIdx));
        LinkedList<C> newColList = new LinkedList<>(cols.subList(rowStartIdx, rowEndIdx));
        
        ArrayMatrix<R, C, V> sliceMatrix = new ArrayMatrix<>(newRowList, newColList);
        
        for(int idx = rowStartIdx; idx < rowEndIdx; idx++) {
            sliceMatrix.data[idx] = Arrays.copyOfRange(data[idx], colStartIdx, colEndIdx);
        }
        
        return sliceMatrix;
    }
    
    @Override
    public ArrayMatrix<R, C, V> slice(Collection<R> rows, Collection<C> cols) {
        
        LinkedList<R> newRowList = new LinkedList<>(rows);
        LinkedList<C> newColList = new LinkedList<>(cols);
        
        ArrayMatrix<R, C, V> sliceMatrix = new ArrayMatrix<>(newRowList, newColList);
        
        //TODO: add index range check.
        for(R row : newRowList) {
            int sliceRowIdx = sliceMatrix.rowIdxes.get(row);
            Integer rowIdx = rowIdxes.get(row);
            for(C col : newColList) {
                int sliceColIdx = sliceMatrix.colIdxes.get(col);
                Integer colIdx = colIdxes.get(col);
                sliceMatrix.data[sliceRowIdx][sliceColIdx] = data[rowIdx][colIdx];
            }
        }
        
        return sliceMatrix;
    }
    
    @Override
    public ArrayMatrix<C, R, V> transpose() {
        ArrayMatrix<C, R, V> transposeMatrix = new ArrayMatrix<>(new LinkedList<>(cols), new LinkedList<>(rows));
        for(int i = 0; i < rows.size(); i++) {
            for(int j = 0; j < cols.size(); j++) {
                transposeMatrix.data[j][i] = data[i][j];
            }
        }
        
        return transposeMatrix;
    }
    
    /****************************************
     *  Matrix Manipulation
     ***************************************
     */
    public ArrayMatrix<R, C, V> plus(ArrayMatrix<R, C, V> other,
                                     BiFunction<V, V, V> plus,
                                     BiFunction<V, V, V> multiply) {
        ArrayMatrix<R, C, V> plusMatrix = new ArrayMatrix<>(new LinkedList<>(this.rows), new LinkedList<>(this.cols));
        for(int i = 0; i < this.rows.size(); i++) {
            for(int j = 0; j < other.cols.size(); j++) {
                plusMatrix.data[i][j] = plus.apply(other.data[i][j], this.data[i][j]);
            }
        }
        return plusMatrix;
    }
    
    public ArrayMatrix<R, C, V> multiply(ArrayMatrix<R, C, V> other,
                                         BiFunction<V, V, V> plus,
                                         BiFunction<V, V, V> multiply) {
        ArrayMatrix<R, C, V> multiplyMatrix = new ArrayMatrix<>(new LinkedList<>(this.rows),
                                                                new LinkedList<>(other.cols));
    
        for(int i = 0; i < this.rows.size(); i++) {
            for(int j = 0; j < other.cols.size(); j++) {
                V val = multiply.apply(this.data[i][0], other.data[0][j]);
                for(int k = 1; k < this.rows.size(); k++) {
                    val = plus.apply(val, multiply.apply(this.data[i][k], other.data[k][j]));
                }
                multiplyMatrix.data[i][j] = val;
            }
        }
    
        return multiplyMatrix;
    }
    
    @Override
    public V putValue(R row, C col, V value) {
        //TODO: add index range check.
        Integer rowIdx = rowIdxes.get(row);
        Integer colIdx = colIdxes.get(col);
        V oldValue = data[rowIdx][colIdx];
        data[rowIdx][colIdx] = value;
        
        return oldValue;
    }
    
    @Override
    public V getValue(R row, C col) {
        Integer rowIdx;
        Integer colIdx;
        return ((rowIdx = rowIdxes.get(row)) == null || (colIdx = colIdxes.get(col)) == null) ? null
                                                                                              : data[rowIdx][colIdx];
    }
    
    public boolean isEmpty() {
        return rows.isEmpty() || cols.isEmpty();
    }
    
    @Override
    public int totalSize() {
        return rows.size() * cols.size();
    }
    
}
