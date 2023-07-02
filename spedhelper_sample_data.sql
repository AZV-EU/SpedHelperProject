--
-- PostgreSQL database dump
--

-- Dumped from database version 15.3
-- Dumped by pg_dump version 15.3

-- Started on 2023-07-02 16:53:48

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3365 (class 0 OID 16411)
-- Dependencies: 215
-- Data for Name: customer; Type: TABLE DATA; Schema: public; Owner: spedhelper
--

COPY public.customer (id, email, fore_name, last_name, password_hash, registration_date, active, authorities) FROM stdin;
1	debug@debugmail.com	Debug	Debug	$2a$10$EF8KFhC9.UjG2TX4n9GRzeH9LtgOy9hvDdylVeUagg..XgE7Shxym	2023-07-02 14:43:57.716942+00	t	ADMIN
\.


--
-- TOC entry 3367 (class 0 OID 16464)
-- Dependencies: 217
-- Data for Name: api_key; Type: TABLE DATA; Schema: public; Owner: spedhelper
--

COPY public.api_key (id, api_key, owner_id, billing_type, last_billing_date, last_request_date, requests_since_last_billing_count, total_requests_count) FROM stdin;
1	debugkey	1	0	2023-07-01 02:22:43.606451+00	2023-07-02 14:46:04.587497+00	230	230
\.


--
-- TOC entry 3364 (class 0 OID 16405)
-- Dependencies: 214
-- Data for Name: vehicle; Type: TABLE DATA; Schema: public; Owner: spedhelper
--

COPY public.vehicle (id, capacity, litresper100, model, odometer, owner_id, range) FROM stdin;
2	1800	7.6	Renault Cargo	112750	1	700
3	1800	7.6	Renault Cargo	57980	1	700
4	1500	6.5	Citroen Transport	187500	1	450
1	1500	6.5	Ford Transit	117426	1	450
\.


--
-- TOC entry 3366 (class 0 OID 16418)
-- Dependencies: 216
-- Data for Name: trip; Type: TABLE DATA; Schema: public; Owner: spedhelper
--

COPY public.trip (id, date, distance, vehicle_id, cargo) FROM stdin;
10	2023-02-13 12:00:00+00	419	1	1900
9	2023-02-12 12:00:00+00	128	1	400
8	2023-02-11 12:00:00+00	687	1	2100
7	2023-02-10 12:00:00+00	452	1	1000
6	2023-02-09 12:00:00+00	468	1	1100
5	2023-02-08 12:00:00+00	278	1	800
4	2023-02-07 12:00:00+00	538	1	2000
3	2023-02-06 12:00:00+00	327	1	1200
2	2023-02-05 12:00:00+00	480	1	1700
1	2023-02-04 12:00:00+00	105	1	900
\.


--
-- TOC entry 3377 (class 0 OID 0)
-- Dependencies: 219
-- Name: api_key_id_seq; Type: SEQUENCE SET; Schema: public; Owner: spedhelper
--

SELECT pg_catalog.setval('public.api_key_id_seq', 8, true);


--
-- TOC entry 3378 (class 0 OID 0)
-- Dependencies: 220
-- Name: customer_id_seq; Type: SEQUENCE SET; Schema: public; Owner: spedhelper
--

SELECT pg_catalog.setval('public.customer_id_seq', 4, true);


--
-- TOC entry 3379 (class 0 OID 0)
-- Dependencies: 221
-- Name: trip_id_seq; Type: SEQUENCE SET; Schema: public; Owner: spedhelper
--

SELECT pg_catalog.setval('public.trip_id_seq', 14, true);


--
-- TOC entry 3380 (class 0 OID 0)
-- Dependencies: 218
-- Name: vehicle_id_seq; Type: SEQUENCE SET; Schema: public; Owner: spedhelper
--

SELECT pg_catalog.setval('public.vehicle_id_seq', 5, true);


-- Completed on 2023-07-02 16:53:49

--
-- PostgreSQL database dump complete
--

