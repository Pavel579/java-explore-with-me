package ru.practicum.ewm.service;

import ru.practicum.ewm.model.EndpointHit;

import javax.servlet.http.HttpServletRequest;

public interface HitService {
    EndpointHit sendHits(HttpServletRequest request);
}
