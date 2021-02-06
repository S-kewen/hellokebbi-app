package com.example.hellokeb_bi.pattern.mediator;

public abstract class Component {
    Mediator mediator;

    public Component(Mediator m) {
        mediator = m;
    }
}
