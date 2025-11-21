package com.tavern.domain.model.repository;

import java.util.List;

public record GetOptions(int offset, int limit, List<Sort> orderedSorts) {
}
