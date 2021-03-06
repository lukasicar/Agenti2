angular.module("oi.select", []), angular.module("oi.select").provider("oiSelect", function () {

    return {

        options: {

            debounce: 500,

            searchFilter: "oiSelectCloseIcon",

            dropdownFilter: "oiSelectHighlight",

            listFilter: "oiSelectAscSort",

            groupFilter: "oiSelectGroup",

            editItem: !1,

            newItem: !1,

            closeList: !0,

            saveTrigger: "enter tab blur"

        }, version: {full: "0.2.20", major: 0, minor: 2, dot: 20}, $get: function () {

            return {options: this.options, version: this.version}

        }

    }

}).factory("oiSelectEscape", function () {

    var e = /[-\/\\^$*+?.()|[\]{}]/g, t = "\\$&";

    return function (n) {

        return String(n).replace(e, t)

    }

}).factory("oiSelectEditItem", function () {

    return function (e, t, n, o) {

        return o ? "" : n(e)

    }

}).factory("oiUtils", ["$document", "$timeout", function (e, t) {

    function n(e, t, n) {

        for (var o = t; o && o.ownerDocument && 11 !== o.nodeType;) {

            if (n) {

                if (o === e)return !1;

                if (o.className.indexOf(n) >= 0)return !0

            } else if (o === e)return !0;

            o = o.parentNode

        }

        return !1

    }



    function o(o, r) {

        function i(e) {

            return e && "INPUT" !== e.target.nodeName ? void 0 : (d = !1, c ? void(d = !0) : void t(function () {

                o.triggerHandler("blur")

            }))

        }



        function s() {

            a || (a = !0, t(function () {

                o.triggerHandler("focus")

            }))

        }



        function l() {

            c = !0

        }



        function u(e) {

            c = !1;

            var s = e.target, l = n(o[0], s);

            d && !l && i(), l && "INPUT" !== s.nodeName && t(function () {

                r[0].focus()

            }), !l && a && (a = !1)

        }



        var a, c, d;

        return e[0].addEventListener("click", u, !0), o[0].addEventListener("mousedown", l, !0), o[0].addEventListener("blur", i, !0), r.on("focus", s), function () {

            e[0].removeEventListener("click", u), o[0].removeEventListener("mousedown", l, !0), o[0].removeEventListener("blur", i, !0), r.off("focus", s)

        }

    }



    function r(e, t) {

        var n, o, r, i, l, a;

        t && (o = e.offsetHeight, r = u(t, "height", "margin"), i = e.scrollTop || 0, n = s(t).top - s(e).top + i, l = n, a = n - o + r, n + r > o + i ? e.scrollTop = a : i > n && (e.scrollTop = l))

    }



    function i(e, t, n, o, r) {

        function i(e) {

            return parseFloat(r[e])

        }



        for (var s = n === (o ? "border" : "content") ? 4 : "width" === t ? 1 : 0, l = 0, u = ["Top", "Right", "Bottom", "Left"]; 4 > s; s += 2)"margin" === n && (l += i(n + u[s])), o ? ("content" === n && (l -= i("padding" + u[s])), "margin" !== n && (l -= i("border" + u[s] + "Width"))) : (l += i("padding" + u[s]), "padding" !== n && (l += i("border" + u[s] + "Width")));

        return l

    }



    function s(e) {

        var t, n, o = e.getBoundingClientRect(), r = e && e.ownerDocument;

        return r ? (t = r.documentElement, n = l(r), {

            top: o.top + n.pageYOffset - t.clientTop,

            left: o.left + n.pageXOffset - t.clientLeft

        }) : void 0

    }



    function l(e) {

        return null != e && e === e.window ? e : 9 === e.nodeType && e.defaultView

    }



    function u(e, t, n) {

        var o = !0, r = "width" === t ? e.offsetWidth : e.offsetHeight, s = window.getComputedStyle(e, null), l = !1;

        if (0 >= r || null == r) {

            if (r = s[t], (0 > r || null == r) && (r = e.style[t]), g.test(r))return r;

            r = parseFloat(r) || 0

        }

        return r + i(e, t, n || (l ? "border" : "content"), o, s)

    }



    function a(e) {

        for (var t in e)if (e.hasOwnProperty(t) && e[t].length)return !1;

        return !0

    }



    function c(e) {

        var t = [];

        return angular.forEach(e, function (e, n) {

            "$" !== n.toString().charAt(0) && t.push(e)

        }), t

    }



    function d(e, t, n, o, r) {

        var i, s, l, u, a, c = r ? [].concat(e) : [];

        for (i = 0, l = e.length; i < e.length; i++)for (u = n ? n(e[i]) : e[i], s = 0; s < t.length; s++)if (a = o ? o(t[s]) : t[s], angular.equals(u, a, e, t, i, s)) {

            r ? c.splice(i + c.length - l, 1) : c.push(t[s]);

            break

        }

        return c

    }



    function p(e, t, n, o) {

        var r = {};

        return e.split(".").reduce(function (e, n, o, r) {

            return e[n] = o < r.length - 1 ? {} : t

        }, r), o(n, r)

    }



    var f = /[+-]?(?:\d*\.|)\d+(?:[eE][+-]?\d+|)/.source, g = new RegExp("^(" + f + ")(?!px)[a-z%]+$", "i");

    return {

        contains: n,

        bindFocusBlur: o,

        scrollActiveOption: r,

        groupsIsEmpty: a,

        objToArr: c,

        getValue: p,

        intersection: d

    }

}]), angular.module("oi.select").directive("oiSelect", ["$document", "$q", "$timeout", "$parse", "$interpolate", "$injector", "$filter", "$animate", "oiUtils", "oiSelect", function (e, t, n, o, r, i, s, l, u, a) {

    var c = /^\s*([\s\S]+?)(?:\s+as\s+([\s\S]+?))?(?:\s+group\s+by\s+([\s\S]+?))?(?:\s+disable\s+when\s+([\s\S]+?))?\s+for\s+(?:([\$\w][\$\w]*)|(?:\(\s*([\$\w][\$\w]*)\s*,\s*([\$\w][\$\w]*)\s*\)))\s+in\s+([\s\S]+?)(?:\s+track\s+by\s+([\s\S]+?))?$/, d = /([^\(\)\s\|\s]*)\s*(\(.*\))?\s*(\|?\s*.+)?/;

    return {

        restrict: "AE", templateUrl: "src/template.html", require: "ngModel", scope: {}, compile: function (e, p) {

            var f = p.oiOptions, g = f ? f.match(c) : ["", "i", "", "", "", "i", "", "", ""];

            if (!g)throw new Error("Expected expression in form of '_select_ (as _label_)? for (_key_,)?_value_ in _collection_'");

            var m, h, v, $, b, w, y, S, k = / as /.test(g[0]) && g[1], I = g[2] || g[1], F = g[5] || g[7], L = g[3] || "", V = g[4] || "", q = g[9] || I, E = g[8].match(d), P = E[1], x = P + (E[3] || ""), C = P + (E[2] || ""), H = k && o(k), O = o(I), A = o(L), D = o(V), T = o(x), _ = o(C), N = o(q), M = r(p.multiplePlaceholder || ""), Q = r(p.placeholder || ""), U = o(p.oiSelectOptions), R = angular.version.major <= 1 && angular.version.minor <= 3;

            return function (e, r, c, d) {

                function p() {

                    ae && r.removeClass("cleanMode"), ae = !1

                }



                function f(e, t) {

                    t = t || 150, r.addClass(e), n(function () {

                        r.removeClass(e)

                    }, t)

                }



                function k() {

                    e.listItemHide = !0, e.inputHide = !1

                }



                function I() {

                    var t = Y(d.$modelValue);

                    e.listItemHide = !t, e.inputHide = t

                }



                function L(t) {

                    u.contains(r[0], t.target, "disabled") || e.output.length >= y && u.contains(r[0], t.target, "select-dropdown") || (e.inputHide && e.removeItem(0), !e.isOpen || !le.closeList || "INPUT" === t.target.nodeName && e.query.length ? J(e.query) : (Z({query: le.editItem && !ae}), e.$evalAsync()))

                }



                function V() {

                    e.isFocused || (e.isFocused = !0, c.disabled || (e.backspaceFocus = !1))

                }



                function q() {

                    e.isFocused = !1, w || I(), E("blur") || Z(), e.$evalAsync()

                }



                function E(o, r) {

                    r || (r = o, o = e.query);

                    var i, s = le.saveTrigger.split(" ").indexOf(r) + 1, l = le.newItem && o, u = "blur" !== r ? e.order[e.selectorPosition] : null;

                    return s && (l || u) ? (e.showLoader = !0, i = t.when(u || S(e.$parent, {$query: o})), i.then(function (o) {

                        if (void 0 === o)return t.reject();

                        e.addItem(o);

                        var r = e.order.length - 1;

                        e.selectorPosition === r && ee(oe, 0), le.newItemFn && !u || n(angular.noop), Z()

                    })["catch"](function () {

                        f("invalid-item"), e.showLoader = !1

                    }), !0) : void 0

                }



                function x() {

                    var e = w && Y(d.$modelValue) ? ie : re;

                    ne.attr("placeholder", e)

                }



                function C(t) {

                    return u.getValue(F, t, e.$parent, N)

                }



                function j(t) {

                    return u.getValue(F, t, e.$parent, H)

                }



                function W(t) {

                    return u.getValue(F, t, e.$parent, O)

                }



                function B(t) {

                    return u.getValue(F, t, e.$parent, D)

                }



                function G(t) {

                    return u.getValue(F, t, e.$parent, A) || ""

                }



                function z(t) {

                    return u.getValue(P, t, e.$parent, T)

                }



                function X(e) {

                    return e = e instanceof Array ? e : e ? [e] : [], e.filter(function (e) {

                        return e && (e instanceof Array && e.length || H || W(e))

                    })

                }



                function Y(e) {

                    return !!X(e).length

                }



                function J(o, i) {

                    return v && ce && n.cancel(v), v = n(function () {

                        var s = _(e.$parent, {$query: o, $selectedAs: i}) || "";

                        return e.selectorPosition = "prompt" === le.newItem ? !1 : 0, o || i || (e.oldQuery = null), (s.$promise && !s.$resolved || angular.isFunction(s.then)) && (ce = le.debounce), e.showLoader = !0, t.when(s.$promise || s).then(function (t) {

                            if (e.groups = {}, t && !i) {

                                var n = w ? e.output : [], s = ve(u.objToArr(t), o, W, $e(e.$parent), r), l = u.intersection(s, n, C, C, !0), a = z(l);

                                e.groups = te(a)

                            }

                            return K(), t

                        })["finally"](function () {

                            e.showLoader = !1, le.closeList && !le.cleanModel && n(function () {

                                ee(oe, 0)

                            })

                        })

                    }, ce)

                }



                function K() {

                    var t, n, o, r = [], i = 0;

                    e.order = [], e.groupPos = {};

                    for (n in e.groups)e.groups.hasOwnProperty(n) && "$" != n.charAt(0) && r.push(n);

                    for (R && r.sort(), t = 0; t < r.length; t++)n = r[t], o = e.groups[n], e.order = e.order.concat(o), e.groupPos[n] = i, i += o.length

                }



                function Z(t) {

                    t = t || {}, e.oldQuery = null, e.backspaceFocus = !1, e.groups = {}, e.order = [], e.showLoader = !1, e.isOpen = !1, ce = 0, t.query || (e.query = ""), v && n.cancel(v)

                }



                function ee(t, n) {

                    e.selectorPosition = n, u.scrollActiveOption(t[0], t.find("li")[n])

                }



                function te(e) {

                    for (var t, n, o = {"": []}, r = 0; r < e.length; r++)t = G(e[r]), (n = o[t]) || (n = o[t] = []), n.push(e[r]);

                    return o

                }



                d.$isEmpty = function (e) {

                    return !Y(e)

                };

                var ne = r.find("input"), oe = angular.element(r[0].querySelector(".select-dropdown")), re = Q(e), ie = M(e), se = U(e.$parent) || {}, le = angular.extend({cleanModel: "prompt" === se.newItem}, a.options, se), ue = le.editItem, ae = "correct" === ue, ce = 0;

                (ue === !0 || "correct" === ue) && (ue = "oiSelectEditItem");

                var de = ue ? i.get(ue) : angular.noop, pe = o(le.removeItemFn);

                g = le.searchFilter.split(":");

                var fe = s(g[0]), ge = o(g[1]);

                g = le.dropdownFilter.split(":");

                var me = s(g[0]), he = o(g[1]);

                g = le.listFilter.split(":");

                var ve = s(g[0]), $e = o(g[1]);

                g = le.groupFilter.split(":");

                var be = s(g[0]), we = o(g[1]);

                S = le.newItemFn ? o(le.newItemFn) : function (e, t) {

                    return (U(t) || {}).newItemModel || t.$query

                }, !le.cleanModel || ue && !ae || r.addClass("cleanMode");

                var ye = u.bindFocusBlur(r, ne);

                angular.isDefined(c.autofocus) && n(function () {

                    ne[0].focus()

                }), angular.isDefined(c.readonly) && ne.attr("readonly", !0), angular.isDefined(c.tabindex) && (ne.attr("tabindex", c.tabindex), r[0].removeAttribute("tabindex")), le.maxlength && ne.attr("maxlength", le.maxlength), c.$observe("disabled", function (t) {

                    ne.prop("disabled", t), w && d.$modelValue && d.$modelValue.length && (e.inputHide = t)

                }), e.$on("$destroy", ye), e.$parent.$watch(c.multipleLimit, function (e) {

                    y = Number(e) || 1 / 0

                }), e.$parent.$watch(c.multiple, function (e) {

                    w = void 0 === e ? angular.isDefined(c.multiple) : e, r[w ? "addClass" : "removeClass"]("multiple")

                }), e.$parent.$watch(c.ngModel, function (n, o) {

                    var r = X(n), i = t.when(r);

                    x(), Y(o) && n !== o && p(), w || I(), H && Y(n) && (i = J(null, n).then(function (e) {

                        return u.intersection(r, e, null, j)

                    }), v = null), w && c.disabled && !Y(n) && (e.inputHide = !1), i.then(function (t) {

                        e.output = t, t.length !== r.length && e.removeItem(t.length)

                    })

                }), e.$watch("query", function (t, n) {

                    E(t.slice(0, -1), t.slice(-1)) || (t === n || e.oldQuery && !t || h || (oe[0].scrollTop = 0, t ? (J(t), e.oldQuery = null) : w && (Z(), h = !0)), h = !1)

                }), e.$watch("groups", function (t) {

                    u.groupsIsEmpty(t) ? e.isOpen = !1 : e.isOpen || c.disabled || (e.isOpen = !0, e.isFocused = !0)

                }), e.$watch("isFocused", function (e) {

                    l[e ? "addClass" : "removeClass"](r, "focused", !R && {tempClasses: "focused-animate"})

                }), e.$watch("isOpen", function (e) {

                    l[e ? "addClass" : "removeClass"](r, "open", !R && {tempClasses: "open-animate"})

                }), e.$watch("showLoader", function (e) {

                    l[e ? "addClass" : "removeClass"](r, "loading", !R && {tempClasses: "loading-animate"})

                }), e.addItem = function (t) {

                    if ($ = e.query, !w || !u.intersection(e.output, [t], C, C).length) {

                        if (e.output.length >= y)return void f("limited");

                        var n = e.groups[G(t)] = e.groups[G(t)] || [], o = H ? j(t) : t;

                        n.splice(n.indexOf(t), 1), w ? d.$setViewValue(angular.isArray(d.$modelValue) ? d.$modelValue.concat(o) : [o]) : (d.$setViewValue(o), I()), u.groupsIsEmpty(e.groups) && (e.groups = {}), w || le.closeList || Z({query: !0}), p(), e.oldQuery = e.oldQuery || e.query, e.query = "", e.backspaceFocus = !1

                    }

                }, e.removeItem = function (n) {

                    c.disabled || w && 0 > n || (b = w ? d.$modelValue[n] : d.$modelValue, t.when(pe(e.$parent, {$item: b})).then(function () {

                        (w || e.inputHide) && (w ? (d.$modelValue.splice(n, 1), d.$setViewValue([].concat(d.$modelValue))) : (k(), le.cleanModel && d.$setViewValue(void 0)), (w || !e.backspaceFocus) && (e.query = de(b, $, W, ae, r) || ""), w && le.closeList && Z({query: !0}))

                    }))

                }, e.setSelection = function (t) {

                    m || e.selectorPosition === t ? m = !1 : ee(oe, t)

                }, e.keyUp = function (t) {

                    switch (t.keyCode) {

                        case 8:

                            e.query.length || w && e.output.length || Z()

                    }

                }, e.keyDown = function (t) {

                    var n = 0, o = e.order.length - 1;

                    switch (t.keyCode) {

                        case 38:

                            e.selectorPosition = angular.isNumber(e.selectorPosition) ? e.selectorPosition : n, ee(oe, e.selectorPosition === n ? o : e.selectorPosition - 1), m = !0;

                            break;

                        case 40:

                            e.selectorPosition = angular.isNumber(e.selectorPosition) ? e.selectorPosition : n - 1, ee(oe, e.selectorPosition === o ? n : e.selectorPosition + 1), m = !0, e.query.length || e.isOpen || J(), e.inputHide && k();

                            break;

                        case 37:

                        case 39:

                            break;

                        case 9:

                            E("tab");

                            break;

                        case 13:

                            E("enter"), t.preventDefault();

                            break;

                        case 32:

                            E("space");

                            break;

                        case 27:

                            w || (I(), le.cleanModel && d.$setViewValue(b)), Z();

                            break;

                        case 8:

                            if (!e.query.length) {

                                if ((!w || ue) && (e.backspaceFocus = !0), e.backspaceFocus && e.output && (!w || e.output.length)) {

                                    e.removeItem(e.output.length - 1), ue && t.preventDefault();

                                    break

                                }

                                e.backspaceFocus = !e.backspaceFocus;

                                break

                            }

                        default:

                            return e.inputHide && k(), e.backspaceFocus = !1, !1

                    }

                }, e.getSearchLabel = function (t) {

                    var n = W(t);

                    return fe(n, e.oldQuery || e.query, t, ge(e.$parent), r)

                }, e.getDropdownLabel = function (t) {

                    var n = W(t);

                    return me(n, e.oldQuery || e.query, t, he(e.$parent), r)

                }, e.getGroupLabel = function (t, n) {

                    return be(t, e.oldQuery || e.query, n, we(e.$parent), r)

                }, e.getDisableWhen = B, Z(), r[0].addEventListener("click", L, !0), r.on("focus", V), r.on("blur", q)

            }

        }

    }

}]), angular.module("oi.select").filter("oiSelectGroup", ["$sce", function (e) {

    return function (t) {

        return e.trustAsHtml(t)

    }

}]).filter("oiSelectCloseIcon", ["$sce", function (e) {

    return function (t) {

        var n = '<span class="close select-search-list-item_selection-remove">×</span>';

        return e.trustAsHtml(t + n)

    }

}]).filter("oiSelectHighlight", ["$sce", "oiSelectEscape", function (e, t) {

    return function (n, o) {

        var r;

        return o.length > 0 || angular.isNumber(o) ? (n = n.toString(), o = t(o.toString()), r = n.replace(new RegExp(o, "gi"), "<strong>$&</strong>")) : r = n, e.trustAsHtml(r)

    }

}]).filter("oiSelectAscSort", ["oiSelectEscape", function (e) {

    function t(t, n, o, r) {

        var i, s, l, u, a = [], c = [], d = [], p = [];

        if (n) {

            for (n = e(String(n)), i = 0, l = !1; i < t.length; i++) {

                if (l = o(t[i]).match(new RegExp(n, "i")), !l && r && (r.length || r.fields))for (s = 0; s < r.length && !l; s++)l = String(t[i][r[s]]).match(new RegExp(n, "i"));

                l && a.push(t[i])

            }

            for (i = 0; i < a.length; i++)o(a[i]).match(new RegExp("^" + n, "i")) ? c.push(a[i]) : d.push(a[i]);

            if (u = c.concat(d), r && (r === !0 || r.all)) {

                e:for (i = 0; i < t.length; i++) {

                    for (s = 0; s < u.length; s++)if (t[i] === u[s])continue e;

                    p.push(t[i])

                }

                u = u.concat(p)

            }

        } else u = [].concat(t);

        return u

    }



    return t

}]).filter("none", function () {

    return function (e) {

        return e

    }

}), angular.module("oi.select").run(["$templateCache", function (e) {

    e.put("src/template.html", '<div class=select-search><ul class=select-search-list><li class="btn btn-default btn-xs select-search-list-item select-search-list-item_selection" ng-hide=listItemHide ng-repeat="item in output track by $index" ng-class="{focused: backspaceFocus && $last}" ng-click=removeItem($index) ng-bind-html=getSearchLabel(item)></li><li class="select-search-list-item select-search-list-item_input" ng-class="{\'select-search-list-item_hide\': inputHide}"><input autocomplete=off ng-model=query ng-keyup=keyUp($event) ng-keydown=keyDown($event)></li><li class="select-search-list-item select-search-list-item_loader" ng-show=showLoader></li></ul></div><div class=select-dropdown ng-show=isOpen><ul ng-if=isOpen class=select-dropdown-optgroup ng-repeat="(group, options) in groups"><div class=select-dropdown-optgroup-header ng-if="group && options.length" ng-bind-html="getGroupLabel(group, options)"></div><li class=select-dropdown-optgroup-option ng-init="isDisabled = getDisableWhen(option)" ng-repeat="option in options" ng-class="{\'active\': selectorPosition === groupPos[group] + $index, \'disabled\': isDisabled, \'ungroup\': !group}" ng-click="isDisabled || addItem(option)" ng-mouseenter="setSelection(groupPos[group] + $index)" ng-bind-html=getDropdownLabel(option)></li></ul></div>')

}]);