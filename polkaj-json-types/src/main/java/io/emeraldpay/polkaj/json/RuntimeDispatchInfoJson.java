package io.emeraldpay.polkaj.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import io.emeraldpay.polkaj.types.DotAmount;

public class RuntimeDispatchInfoJson {

    @JsonProperty("class")
    private DispatchClass dispatchClass;
    private DotAmount partialFee;
    private Weight weight;

    public DispatchClass getDispatchClass() {
        return dispatchClass;
    }

    public void setDispatchClass(DispatchClass dispatchClass) {
        this.dispatchClass = dispatchClass;
    }

    public DotAmount getPartialFee() {
        return partialFee;
    }

    public void setPartialFee(DotAmount partialFee) {
        this.partialFee = partialFee;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RuntimeDispatchInfoJson)) return false;
        RuntimeDispatchInfoJson that = (RuntimeDispatchInfoJson) o;
        return dispatchClass == that.dispatchClass &&
                Objects.equals(partialFee, that.partialFee) &&
                Objects.equals(weight, that.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dispatchClass, partialFee, weight);
    }

    public static enum DispatchClass {
        @JsonProperty("normal")
        NORMAL,
        @JsonProperty("operational")
        OPERATIONAL,
        @JsonProperty("mandatory")
        MANDATORY
    }

    public static class Weight {
        @JsonProperty("ref_time")
        Long refTime;
    }
}
