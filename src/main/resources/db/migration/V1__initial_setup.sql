create table rental_agreement
(
    id                  bigint not null auto_increment,
    brand               varchar(255),
    charge_days         integer,
    checkout_date       date,
    daily_rental_charge float(53),
    discount_amount     float(53),
    discount_percent    integer,
    due_date            date,
    final_charge        float(53),
    pre_discount_charge float(53),
    rental_days         integer,
    tool_code           varchar(255),
    tool_type           enum ('CHAINSAW','LADDER','JACK_HAMMER'),
    primary key (id)
);

create table tool_type_charges
(
    tool_type      enum ('CHAINSAW','LADDER','JACK_HAMMER') not null,
    daily_charge   float(53),
    weekday_charge bit,
    weekend_charge bit,
    holiday_charge bit,
    primary key (tool_type)
);

create table tool
(
    tool_code         varchar(255) not null,
    tool_type_charges enum ('CHAINSAW','LADDER','JACK_HAMMER'),
    brand             varchar(255),
    primary key (tool_code),
    foreign key (tool_type_charges) references tool_type_charges (tool_type)
);


-- Inserting sample data into the ToolTypeCharges table
INSERT INTO tool_type_charges (tool_type, daily_charge, weekday_charge, weekend_charge, holiday_charge)
VALUES ('LADDER', 1.99, 1, 1, 0),
       ('CHAINSAW', 1.49, 1, 0, 1),
       ('JACK_HAMMER', 2.99, 1, 0, 0);

-- Inserting sample data into the Tool table
INSERT INTO tool(tool_code, tool_type_charges, brand)
VALUES ('CHNS', 'CHAINSAW', 'Stihl'),
       ('LADW', 'LADDER', 'Werner'),
       ('JAKD', 'JACK_HAMMER', 'DeWalt'),
       ('JAKR', 'JACK_HAMMER', 'Ridgid');

