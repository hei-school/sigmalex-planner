# SigmaLex Planner

A dark project that
captures [the dark timetabling constraints of the school](https://github.com/hei-school/sigma-lex/tree/main/src/main/java/school/hei/planner/constraint/sub)
... and solves them, in a dark manner!

The name SigmaLex comes from the eponym Domain Specific Language that we use
to express the different constraints as a mathematician would.
That is, express constraints declaratively rather than imperatively.

## Try it out locally

1. Clone the project
2. Run
   the [WebPlanner](https://github.com/hei-school/hei-planner/blob/main/src/main/java/school/hei/planner/web/WebPlanner.java)
   class
3. Browse [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)
4. Submit and solve [feasible-timetable1.json](feasible-timetable1.json)
5. Fear the dark magic that provided the solution!

## Try it out online

1. `POST https://65t7ecavfvc5l2uwxi2jdwvkji0aohxd.lambda-url.eu-west-3.on.aws/timetable`
   with [feasible-timetable1.json](feasible-timetable1.json) as payload
2. Fear the dark magic that provided the solution... in a serverless manner!
