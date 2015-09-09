## What

A very much _work in progress_ example of writing a test suite against
an Infrastructure as a Service, in this case
[DigitalOcean](https://www.digitalocean.com/?refcode=69ef0beac642).

## Why

TDD, ie. using tests to help describe and design your code, is a
well-practices approach to software development. If we really are
treating out _infrastructure as code_ then how about a test suite
picking on certain aspects of a provisioned infrastructure as a service?

This example completely separates the actual testing from the creation
of virtual resources. This isn't a declarative provisioning tool. The
utility I see with that approach is that you could write a simple test
suite and then anyone can create an environment to pass those tests.
This makes certain aspects of portability easier, as well as greatly
simplying the testing half.

## How

The tests are just clojure, although you could implement the same idea
in any language with a suitable IaaS client. The syntax uses
[expectations](http://jayfields.com/expectations/) which leads to some
pretty expressive code:

```clojure
(expect 2 (count nodes))
```

The current [full test
suite](https://github.com/garethr/digitalocean-expect/blob/master/test/clojure/digitalocean_expect/test.clj)
is available in the repository.

## Running

In order to run the tests yourself you'll need a DigitalOcean account.
The underlying client uses the V2 API so you need to set a
`DIGITALOCEAN-ACCESS-TOKEN` environment variables. Alternatively you can use
`.lein-env` if you know what you're doing.

You can run the tests with the following. Note that the specific tests
here are unlikely to pass with anything but the small test dataset I
was using.

    lein expectations

Much more fun is running the tests everytime you make a change to the
test file.

    lein autoexpect

## Thanks

Thanks to the [digitalocean](https://github.com/owainlewis/digital-ocean)
Clojure client for dealing with the DigitalOcean interface so I could get
on with the job at hand.
