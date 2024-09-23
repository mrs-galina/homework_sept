package util;

public enum TypesOfSort {
    дешевле("дешевле"),
    дороже("дороже"),
    по_дате("по дате");

    public String value;

    public String getValue() {
        return value;
    }

    TypesOfSort(String value) {
        this.value = value;
    }
}
