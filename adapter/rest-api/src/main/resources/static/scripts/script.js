$(document).ready(function () {
    $('#loading-spinner').hide();
});

$(document).ready(function () {
    $('#generated-images-wrapper').hide();
});

let automaton = null;

const regexNotationType = {
    COMMON: 'COMMON',
    SIMPLE: 'SIMPLE',
};

const programmingLanguages = {
    C: 'C',
    JAVA: 'JAVA',
    PYTHON: 'PYTHON'
};

window.addEventListener("load", start);

function start() {
    const generateRandomRegexButton = document.getElementById("generate-random-regex-button");
    const generateGraphsButton = document.getElementById("generate-graphs-button");
    const generateAndDownloadCodesButton = document.getElementById("generate-and-download-codes-button");
    const changeLanguageSwitch = document.getElementById("pageLanguage");
    const regexInput = document.getElementById('regex');
    generateRandomRegexButton.addEventListener("click", (e) => generateRandomRegex(e));
    generateGraphsButton.addEventListener("click", (e) => generateGraphs(e));
    generateAndDownloadCodesButton.addEventListener("click", (e) => generateAndDownloadCodes(e));
    changeLanguageSwitch.addEventListener("change", (e) => changePageLanguage(e));
    regexInput.addEventListener('input', (e) => regexHasBeenChanged(e));
}

window.addEventListener('load', updateIconWheelGapSize);
window.addEventListener('resize', updateIconWheelGapSize);

function updateIconWheelGapSize() {
    const generateRandomRegexButton = document.getElementById("generate-random-regex-button");
    const gearWrapper = document.querySelector('.gear-wrapper');
    gearWrapper.style.display = "";
    const buttonWidth = generateRandomRegexButton.offsetWidth + 10;
    const marginValue = buttonWidth - 1;

    gearWrapper.style.marginLeft = `${marginValue}px`;
}

function changePageLanguage(e) {
    e.preventDefault();

    let notationType = getNotationType();

    const headerText = document.querySelector(".main-header-text");
    const regexPlaceHolder = document.querySelector('label[for="regex"]');
    const regexNumOfSymbols = document.querySelector('label[for="regexNumOfSymbols"]');
    const regexSymbolsToUse = document.querySelector('label[for="regexSymbolsToUse"]');
    const textBox1 = document.querySelector("#text-box-1");
    const textBox2 = document.querySelector("#text-box-2");
    const textBox3 = document.querySelector("#text-box-3");
    const textBox4 = document.querySelector("#text-box-4");
    const randomRegexButton = document.querySelector("#generate-random-regex-button");
    const generateGraphsButton = document.querySelector("#generate-graphs-button");
    const transitionDiagramText = document.querySelector("#Transition-Diagram-text");
    const transitionTableText = document.querySelector("#Transition-Table-text");
    const DFAGraphText = document.querySelector("#DFA-Graph-text");
    const automatonHasBeenGenerated = document.querySelector("#automatonHasBeenGenerated");
    const downloadDFAGraphButton = document.querySelector("#downloadDFAGraphButton");
    const downloadTransitionGraphButton = document.querySelector("#downloadTransitionGraphButton");
    const downloadTransitionTableButton = document.querySelector("#downloadTransitionTableButton");
    const toggle = document.getElementById("pageLanguage");
    if (toggle.checked) { // EN
        headerText.innerHTML = "\n        <span>Generator of acceptors for regular expressions</span>\n    ";
        textBox1.innerHTML = "\n                    <div class=\"box-content-wrapper\">\n                        <div class=\"box-content\">\n                            Introduction\n                        </div>\n                        <hr>\n                        <div class=\"box-content\">\n                            This tool is used to generate a transition diagram,\n                            DKA graph and source code in programming languages such as C,\n                            JAVA and Python based on an input regular expression.\n                            The generated codes mimic the acceptor for the given input regular expression.\n                        </div>\n                    </div>\n                ";
        textBox2.innerHTML = "\n                    <div class=\"box-content-wrapper\">\n                        <div class=\"box-content\">\n                            Introduction\n                        </div>\n                        <hr>\n                        <div class=\"box-content\">\n                            The input regular expression can only contain numbers,\n                            letters and special symbols of the selected notation.\n                        </div>\n                    </div>\n                ";
        textBox3.innerHTML = "\n                    <div class=\"box-content-wrapper\">\n                        Simplified notation:\n                        <div class=\"box-content\">\n                            <div>r = (xy)</div>\n                            <div>r = x|y</div>\n                            <div>r = {x}</div>\n                            <div>r = x{x}</div>\n                            <div>r = [x]</div>\n                        </div>\n                    </div>\n                    <form class=\"checkbox-wrapper\">\n                        <input id=\"checkbox-simple-notation-type\" type=\"checkbox\" name=\"notation-type\" onclick=\"disableCommonNotationType()\" value=\"simple\">\n                    </form>\n                ";
        textBox4.innerHTML = "\n                    <div class=\"box-content-wrapper\">\n                        Notation common in implementations:\n                        <div class=\"box-content\">\n                            <div>r = (xy)</div>\n                            <div>r = x|y</div>\n                            <div>r = (x)*</div>\n                            <div>r = (x)+</div>\n                            <div>r = (x)?</div>\n                        </div>\n                    </div>\n                    <form>\n                        <div class=\"checkbox-wrapper\">\n                            <input id=\"checkbox-common-notation-type\" type=\"checkbox\" name=\"notation-type\" onclick=\"disableSimpleNotationType()\" value=\"common\">\n                        </div>\n                    </form>\n                ";
        regexPlaceHolder.outerHTML = "<label for=\"regex\" class=\"input-text\">Enter a regular expression:</label>";
        regexNumOfSymbols.outerHTML = "<label for=\"regexNumOfSymbols\" class=\"input-text\">Enter the number of symbols (0-50):</label>";
        regexSymbolsToUse.outerHTML = "<label for=\"regexSymbolsToUse\" class=\"input-text\">Enter the symbols themselves (a-z, A-Z, 0-9):</label>";
        randomRegexButton.innerHTML = "Random regex";
        generateGraphsButton.innerHTML = "Generate";
        transitionDiagramText.innerHTML = "Transition diagram";
        transitionTableText.innerHTML = "Transition table";
        DFAGraphText.innerHTML = "DFA Graph";
        downloadDFAGraphButton.innerHTML = "Download";
        downloadTransitionGraphButton.innerHTML = "Download";
        downloadTransitionTableButton.innerHTML = "Download";
        automatonHasBeenGenerated.innerHTML = "\n        <span>Select the language(s) in which you would like to download the code:</span>\n        <form>\n            <div class=\"checkbox-wrapper\">\n                <input id=\"checkbox-c\" type=\"checkbox\" name=\"languages-to-download\" onclick=\"checkboxDownloadCodeCheck()\" value=\"C\">\n                <label for=\"checkbox-c\">C</label>\n            </div>\n            <div class=\"checkbox-wrapper\">\n                <input id=\"checkbox-java\" type=\"checkbox\" name=\"languages-to-download\" onclick=\"checkboxDownloadCodeCheck()\" value=\"Java\">\n                <label for=\"checkbox-java\">Java</label>\n            </div>\n            <div class=\"checkbox-wrapper\">\n                <input id=\"checkbox-python\" type=\"checkbox\" name=\"languages-to-download\" onclick=\"checkboxDownloadCodeCheck()\" value=\"Python\">\n                <label for=\"checkbox-python\">Python</label>\n            </div>\n            <button id=\"generate-and-download-codes-button\" type=\"button\" class=\"button\">Download\n            </button>\n        </form>\n    ";
    } else { // sk
        headerText.innerHTML = "\n        <span>Generátor akceptorov pre regulárne výrazy</span>\n    ";
        textBox1.innerHTML = "\n                    <div class=\"box-content-wrapper\">\n                        <div class=\"box-content\">\n                            Úvod\n                        </div>\n                        <hr>\n                        <div class=\"box-content\">\n                            Tento nástroj slúži na generovanie prechodového diagramu,\n                            grafu DKA a zdrojového kódu v programovacích jazykoch, ako sú C,\n                            JAVA a Python, na základe vstupného regulárneho výrazu.\n                            Generované kódy napodobňujú akceptor pre daný vstupný regex.\n                        </div>\n                    </div>\n                ";
        textBox2.innerHTML = "\n                    <div class=\"box-content-wrapper\">\n                        <div class=\"box-content\">\n                            Úvod\n                        </div>\n                        <hr>\n                        <div class=\"box-content\">\n                            Vstupný regulárny výraz môže obsahovať iba čísla,\n                            písmená a špeciálne symboly zvoleného zápisu.\n                        </div>\n                    </div>\n                ";
        textBox3.innerHTML = "\n                    <div class=\"box-content-wrapper\">\n                        Zjednodušený zápis:\n                        <div class=\"box-content\">\n                            <div>r = (xy)</div>\n                            <div>r = x|y</div>\n                            <div>r = {x}</div>\n                            <div>r = x{x}</div>\n                            <div>r = [x]</div>\n                        </div>\n                    </div>\n                    <form class=\"checkbox-wrapper\">\n                        <input id=\"checkbox-simple-notation-type\" type=\"checkbox\" name=\"notation-type\" onclick=\"disableCommonNotationType()\" value=\"simple\">\n                    </form>\n                ";
        textBox4.innerHTML = "\n                    <div class=\"box-content-wrapper\">\n                        Zápis bežný v implementáciách:\n                        <div class=\"box-content\">\n                            <div>r = (xy)</div>\n                            <div>r = x|y</div>\n                            <div>r = (x)*</div>\n                            <div>r = (x)+</div>\n                            <div>r = (x)?</div>\n                        </div>\n                    </div>\n                    <form>\n                        <div class=\"checkbox-wrapper\">\n                            <input id=\"checkbox-common-notation-type\" type=\"checkbox\" name=\"notation-type\" onclick=\"disableSimpleNotationType()\" value=\"common\">\n                        </div>\n                    </form>\n                ";
        regexPlaceHolder.outerHTML = "<label for=\"regex\" class=\"input-text\">Zadajte regulárny výraz:</label>";
        regexNumOfSymbols.outerHTML = "<label for=\"regexNumOfSymbols\" class=\"input-text\">Zadajte počet symbolov (0-50):</label>";
        regexSymbolsToUse.outerHTML = "<label for=\"regexSymbolsToUse\" class=\"input-text\">Zadajte samotné symboly (a-z, A-Z, 0-9):</label>";
        randomRegexButton.innerHTML = "Náhodný regex";
        generateGraphsButton.innerHTML = "Vygenerovať";
        transitionDiagramText.innerHTML = "Prechodový diagram";
        transitionTableText.innerHTML = "Prechodová tabuľka";
        DFAGraphText.innerHTML = "Graf DKA";
        downloadDFAGraphButton.innerHTML = "Stiahnuť";
        downloadTransitionGraphButton.innerHTML = "Stiahnuť";
        downloadTransitionTableButton.innerHTML = "Stiahnuť";
        automatonHasBeenGenerated.innerHTML = "\n        <span>Vyberte jazyk(y), v ktorom by ste si radi stiahli kód:</span>\n        <form>\n            <div class=\"checkbox-wrapper\">\n                <input id=\"checkbox-c\" type=\"checkbox\" name=\"languages-to-download\" onclick=\"checkboxDownloadCodeCheck()\" value=\"C\">\n                <label for=\"checkbox-c\">C</label>\n            </div>\n            <div class=\"checkbox-wrapper\">\n                <input id=\"checkbox-java\" type=\"checkbox\" name=\"languages-to-download\" onclick=\"checkboxDownloadCodeCheck()\" value=\"Java\">\n                <label for=\"checkbox-java\">Java</label>\n            </div>\n            <div class=\"checkbox-wrapper\">\n                <input id=\"checkbox-python\" type=\"checkbox\" name=\"languages-to-download\" onclick=\"checkboxDownloadCodeCheck()\" value=\"Python\">\n                <label for=\"checkbox-python\">Python</label>\n            </div>\n            <button id=\"generate-and-download-codes-button\" type=\"button\" class=\"button\">Stiahnuť\n            </button>\n        </form>\n    ";
    }
    // back notationType was chosen, if was.
    if (notationType === regexNotationType.COMMON) {
        const commonNotationType = document.getElementById("checkbox-common-notation-type");
        const simpleNotationType = document.getElementById("checkbox-simple-notation-type");
        simpleNotationType.checked = false;
        commonNotationType.checked = true;
        // hide error about not selected notation after select
        errorDisplayed = false;
        document.getElementById("error-in-center").style.display = "none";
    } else if (notationType === regexNotationType.SIMPLE) {
        const commonNotationType = document.getElementById("checkbox-common-notation-type");
        const simpleNotationType = document.getElementById("checkbox-simple-notation-type");
        commonNotationType.checked = false;
        simpleNotationType.checked = true;
        // hide error about not selected notation after select
        errorDisplayed = false;
        document.getElementById("error-in-center").style.display = "none";
    }
    // fix not working button after change language :)
    const generateAndDownloadCodesButton = document.getElementById("generate-and-download-codes-button");
    generateAndDownloadCodesButton.addEventListener("click", (e) => generateAndDownloadCodes(e));
}

async function generateAndDownloadCodes(e) {
    e.preventDefault();

    let selectedLanguagesToDownload = getLanguagesToDownload();

    if (selectedLanguagesToDownload === null) {
        const toggle = document.getElementById("pageLanguage");
        if (toggle.checked) { // EN
            showError('Please, select the language(s) in which you would like to download the code.', 'error-in-bottom');
        } else {
            showError('Prosím, Vyberte jazyk(y), v ktorom by ste si radi stiahli kód.', 'error-in-bottom');
        }
        return;
    }

    let requestBody = {
        automaton: automaton,
        languagesToDownloadCode: selectedLanguagesToDownload,
    };

    $.ajax({
        url: "/v1/regex-to-acceptor/generate-codes",
        type: "POST",
        data: JSON.stringify(requestBody),
        contentType: "application/json",
        xhrFields: {
            responseType: "blob",
        },
        success: function (response, status, xhr) {
            errorDisplayed = false;
            document.getElementById("error-in-bottom").style.display = "none";

            let contentDisposition = xhr.getResponseHeader("Content-Disposition");
            let contentType = xhr.getResponseHeader("Content-Type");
            let file_name = contentDisposition.match(
                /filename[^;=\n]*=(['"]?)(.*)\1/
            )[2];
            let blob = new Blob([response], {type: contentType});
            let downloadUrl = URL.createObjectURL(blob);
            let a = document.createElement("a");
            a.href = downloadUrl;
            a.download = file_name;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
        },
        error: function () {
            const toggle = document.getElementById("pageLanguage");
            if (toggle.checked) { // EN
                showError('Please, select the language(s) in which you would like to download the code.', 'error-in-bottom');
            } else {
                showError('Prosím, Vyberte jazyk(y), v ktorom by ste si radi stiahli kód.', 'error-in-bottom');
            }
        },
    });
}


function showRegexSettings() {
    let regexSettings = document.getElementById("fields-container");
    if (regexSettings.style.display === "none") {
        regexSettings.style.display = "block";
        // for mobiles ux-ui - if still not generated
        if (!(document.getElementById("automatonHasBeenGenerated").style.display === "block")) {
            let regexNumOfSymbolsLine = document.getElementById("regexNumOfSymbols");
            regexNumOfSymbolsLine.scrollIntoView({behavior: 'smooth'})
        }
    } else {
        regexSettings.style.display = "none";
        document.getElementById('regexNumOfSymbols').value = "";
        document.getElementById('regexSymbolsToUse').value = "";
    }
}

async function generateRandomRegex(e) {
    e.preventDefault();

    // delete regex which was before generating
    document.getElementById("regex").value = "";

    // hide already generated things
    $('#generated-images-wrapper').hide();
    $("#automatonHasBeenGenerated").hide();


    let notationType = getNotationType();

    const toggle = document.getElementById("pageLanguage");
    if (notationType === null) {
        if (toggle.checked) { // EN
            showError('Please, select the regular expression notation type.', 'error-in-center');
        } else {
            showError('Prosím, vyberte si typ notácie regulárneho výrazu.', 'error-in-center');
        }
        return;
    }

    // handle manual user input
    const regexNumOfSymbols = document.getElementById('regexNumOfSymbols').value;
    const regexSymbolsToUse = document.getElementById('regexSymbolsToUse').value;
    if (regexNumOfSymbols === "" && regexSymbolsToUse !== "") {
        if (toggle.checked) { // EN
            showError('Please, also specify the desired number of symbols to use. ', 'error-in-center');
        } else {
            showError('Prosím, uveďte aj želaný počet symbolov na použitie.', 'error-in-center');
        }
        return;
    } else if (regexNumOfSymbols !== "" && regexSymbolsToUse === "") {
        if (toggle.checked) { // EN
            showError('Please, also specify the desired symbols to use.', 'error-in-center');
        } else {
            showError('Prosím, uveďte aj želané symboly na použitie.', 'error-in-center');
        }
        return;
    }

    if (regexNumOfSymbols !== "" && regexSymbolsToUse !== "") {
        if (regexNumOfSymbols > 50) {
            if (toggle.checked) { // EN
                showError('The maximum number of symbols possible for manually generating a random regex is 50. Please, provide a smaller value.', 'error-in-center');
            } else {
                showError('Maximálny možný počet symbolov na manuálne vygenerovanie náhodného regulárneho výrazu je 50. Prosím, zadajte menšiu hodnotu.', 'error-in-center');
            }
            return;
        } else if (regexNumOfSymbols < 1) {
            if (toggle.checked) { // EN
                showError('The minimum number of symbols required for manually generating a random regex is 1. Please, provide a larger value.', 'error-in-center');
            } else {
                showError('Minimálny počet symbolov požadovaných na manuálne vygenerovanie náhodného regulárneho výrazu je 1. Prosím, zadajte väčšiu hodnotu.', 'error-in-center');
            }
            return;
        }
        if (!(/^\d+$/.test(regexNumOfSymbols))) { // if input is not only digit
            if (toggle.checked) { // EN
                showError('Please, use only numbers in the field for the number of symbols.', 'error-in-center');
            } else {
                showError('Prosím, použite iba čísla v poli pre počet symbolov.', 'error-in-center');
            }
            return;
        }
    }

    let requestBody = {
        notationType: notationType,
        numberOfSymbols: regexNumOfSymbols,
        symbolsToUse: regexSymbolsToUse
    };

    await $.ajax({
        url: '/v1/regex-to-acceptor/generate-random-regex',
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify(requestBody),
        success: function (response) {

            // hide error
            errorDisplayed = false;
            document.getElementById("error-in-center").style.display = "none";
            document.getElementById("error-in-bottom").style.display = "none";

            // hide loader
            const loader = document.querySelector('.loader');
            loader.style.display = 'none';
            $("#loading-spinner").hide();

            document.getElementById("regex").value = response.regex;
        },
        error: function () {
            // show loading spinner
            const loader = document.querySelector('.loader');
            loader.style.display = '';
            $("#loading-spinner").show();

            generateRandomRegex(e)
        }
    });
}

function regexHasBeenChanged(e) {
    e.preventDefault();

    inputFieldToDefaultStyle();

    errorDisplayed = false;
    document.getElementById("error-in-center").style.display = "none";
    clearTimeout(mainErrorTimeout);
    clearTimeout(changeOpacityErrorTimeout);
}


async function generateGraphs(e) {
    e.preventDefault();

    let regex = document.getElementById("regex").value;

    let notationType = getNotationType();
    if (notationType === null) {
        const toggle = document.getElementById("pageLanguage");
        if (toggle.checked) { // EN
            showError('Please, select the regular expression notation type.', 'error-in-center');
        } else {
            showError('Prosím, vyberte si typ notácie regulárneho výrazu.', 'error-in-center');
        }
        return;
    }

    const requestBody = {
        notationType: notationType,
    };

    // hide previous errors, graphs and download box while generating new
    $("#automatonHasBeenGenerated").hide();
    errorDisplayed = false;
    document.getElementById("error-in-center").style.display = "none";
    document.getElementById("error-in-bottom").style.display = "none";
    document.getElementById("generated-images-wrapper").style.display = "none";
    $("#generated-images-wrapper").hide();

    // show loading spinner
    const loader = document.querySelector('.loader');
    loader.style.display = '';
    $("#loading-spinner").show();

    await $.ajax({
            url:
                "/v1/regex-to-acceptor/generate-graphs?regex=" + encodeURIComponent(regex),
            type: "POST",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(requestBody),
            success: function (response) {
                // hide error
                errorDisplayed = false;
                document.getElementById("error-in-center").style.display = "none";
                document.getElementById("error-in-bottom").style.display = "none";

                // hide loader
                const loader = document.querySelector('.loader');
                loader.style.display = 'none';
                $("#loading-spinner").hide();

                inputFieldToDefaultStyle();

                automaton = {
                    alphabet: response.alphabet,
                    stateCount: response.stateCount,
                    startState: response.startState,
                    acceptStates: response.acceptStates,
                    transitions: response.transitions,
                    dfaToMinDfaStateTransitions: response.dfaToMinDfaStateTransitions,
                };

                let transitionPNGGraph = response.transitionPNGGraph;
                $("#TransitionGraph").attr(
                    "src",
                    `data:image/png; base64, ${transitionPNGGraph}`
                );
                resetImagePositionToDefault("TransitionGraph");

                document.getElementById('TransitionTable').innerHTML = (response.transitionTable);
                resetImagePositionToDefault("TransitionTable");

                let dfaPNGGraph = response.dfaPNGGraph;
                $("#DFAGraph").attr(
                    "src",
                    `data:image/png; base64, ${dfaPNGGraph}`
                );
                resetImagePositionToDefault("DFAGraph");

                enableImageMoveAndZoom("TransitionGraph");
                enableImageMoveAndZoom("TransitionTable");
                enableImageMoveAndZoom("DFAGraph");

                // show new generated graphs and download box
                document.getElementById("generated-images-wrapper").style.display = "block";
                $("#generated-images-wrapper").show();
                $("#automatonHasBeenGenerated").show();

                // scroll to the cente when image regenerated
                let imageHeight = $("#TransitionGraph").height();
                let windowHeight = $(window).height();
                let scrollPosition = $("#TransitionGraph").offset().top - ((windowHeight - imageHeight) / 2);
                $('html, body').animate({
                    scrollTop: scrollPosition
                }, 1000);

                document.getElementById("automatonHasBeenGenerated").style.display =
                    "block";
            },
            error: function (status) {
                // hide loader
                const loader = document.querySelector('.loader');
                loader.style.display = 'none';
                $("#loading-spinner").hide();

                // hide previous select language(s) of code to download error
                document.getElementById("error-in-bottom").style.display = "none";

                const toggle = document.getElementById("pageLanguage");

                if (status.status === 400) {
                    if (toggle.checked) { // EN
                        showError('The size of the regular expression is too large. Please, provide a shorter regular expression.', 'error-in-center');
                    } else {
                        showError('Veľkosť regulárneho výrazu je príliš veľká. Prosím, zadajte kratší regulárny výraz.', 'error-in-center');
                    }
                } else {
                    if (toggle.checked) { // EN
                        showError('Invalid regex pattern. Please, check the syntax and rewrite the expression.', 'error-in-center');
                    } else {
                        showError('Neplatný vzor regulárneho výrazu. Prosím, skontrolujte syntax a prepíšte výraz.', 'error-in-center');
                    }
                }

                let inputField = document.querySelector(".input-line");
                inputField.style.borderColor = "#a80000";

                let inputLabel = document.querySelector(".input-text");
                inputLabel.style.color = "#a80000";
            },
        }
    );
}

//download image button
window.addEventListener('load', function () {
    let downloadButton = document.getElementById('downloadTransitionTableButton');

    downloadButton.addEventListener('click', function () {
        let tableContent = document.getElementById('TransitionTable').innerHTML;

        let tempElement = document.createElement('div');
        tempElement.innerHTML = tableContent;
        tempElement.style.position = 'absolute';
        tempElement.style.left = '-9999px';

        document.body.appendChild(tempElement);
        html2canvas(tempElement).then(function (canvas) {
            let link = document.createElement('a');
            link.href = canvas.toDataURL('image/png');
            link.download = 'TransitionTable.png';
            link.click();
            document.body.removeChild(tempElement);
        });
    });
});

//download image button
window.addEventListener('load', function () {
    let downloadDFAGraphButton = document.getElementById('downloadTransitionGraphButton');

    downloadDFAGraphButton.addEventListener('click', function () {
        let dfaGraphImage = document.getElementById('TransitionGraph');

        let tempElement = document.createElement('img');
        tempElement.src = dfaGraphImage.src; // Set the source of the img element

        tempElement.style.position = 'absolute';
        tempElement.style.left = '-9999px';

        document.body.appendChild(tempElement);
        html2canvas(tempElement).then(function (canvas) {
            let link = document.createElement('a');
            link.href = canvas.toDataURL('image/png');
            link.download = 'TransitionDiagram.png';
            link.click();
            document.body.removeChild(tempElement);
        });
    });
});

//download image button
window.addEventListener('load', function () {
    let downloadDFAGraphButton = document.getElementById('downloadDFAGraphButton');

    downloadDFAGraphButton.addEventListener('click', function () {
        let dfaGraphImage = document.getElementById('DFAGraph');

        let tempElement = document.createElement('img');
        tempElement.src = dfaGraphImage.src; // Set the source of the img element

        tempElement.style.position = 'absolute';
        tempElement.style.left = '-9999px';

        document.body.appendChild(tempElement);
        html2canvas(tempElement).then(function (canvas) {
            let link = document.createElement('a');
            link.href = canvas.toDataURL('image/png');
            link.download = 'GraphDFA.png';
            link.click();
            document.body.removeChild(tempElement);
        });
    });
});

function inputFieldToDefaultStyle() {
    let inputField = document.querySelector(".input-line");
    inputField.style.borderColor = "#2B3649FF";

    let inputLabel = document.querySelector(".input-text");
    inputLabel.style.color = "rgba(0, 0, 0, 0.6)";
    inputField.style.borderColor = "";
    inputLabel.style.color = "";
    inputLabel.style.position = "";
    inputLabel.style.left = "";
    inputLabel.style.bottom = "";
    inputLabel.style.pointerEvents = "";
    inputLabel.style.transition = "";
    inputField.style.border = "";
    inputField.style.background = "";
    inputField.style.width = "";
    inputField.style.padding = "";
}

function resetImagePositionToDefault(imageId) {
    let image = document.getElementById(imageId);
    image.style.transform = "none";
}

function enableImageMoveAndZoom(imageId) {
    let headerHeight = document.querySelector("header").clientHeight;
    let scale = 1,
        panning = false,
        pointX = 0,
        pointY = 0,
        start = {x: 0, y: 0 - headerHeight},
        generatedImagesWrapper = document.getElementById(imageId),
        container = generatedImagesWrapper.parentNode;

    function setTransform() {
        generatedImagesWrapper.style.transform = "translate(" + pointX + "px, " + pointY + "px) scale(" + scale + ")";
    }

    container.onmousedown = function (e) {
        e.preventDefault();
        start = {x: e.clientX - pointX, y: e.clientY - pointY};
        panning = true;
    }

    container.onmouseup = function () {
        panning = false;
        start = {x: pointX, y: pointY};
    }

    container.onmousemove = function (e) {
        e.preventDefault();
        if (!panning) {
            return;
        }
        pointX = (e.clientX - start.x);
        pointY = (e.clientY - start.y);
        setTransform();
    }
    // // TODO: temporary off because of bugs with touchpad
    // container.onwheel = function(e) {
    //     e.preventDefault();
    //     var xs = (e.clientX - pointX) / scale,
    //         ys = (e.clientY - pointY) / scale,
    //         delta = (e.wheelDelta ? e.wheelDelta : -e.deltaY),
    //         prevScale = scale,
    //         prevPointX = pointX,
    //         prevPointY = pointY;
    //     (delta > 0) ? (scale *= 1.2) : (scale /= 1.2);
    //     pointX = e.clientX - xs * scale;
    //     pointY = e.clientY - ys * scale;
    //
    //     var dx = (prevPointX - pointX) / prevScale;
    //     var dy = (prevPointY - pointY) / prevScale;
    //     pointX += dx * scale;
    //     pointY += dy * scale;
    //
    //     setTransform();
    // };
}

function getNotationType() {
    const checkboxes = document.getElementsByName("notation-type");
    let notationType = null;
    checkboxes.forEach(checkbox => {
        if (checkbox.checked)
            notationType = regexNotationType[checkbox.value.toUpperCase()];
    });

    return notationType;
}

function getLanguagesToDownload() {
    const checkboxes = document.getElementsByName("languages-to-download");
    const selectedLanguages = [];
    checkboxes.forEach((checkbox) => {
        if (checkbox.checked) {
            selectedLanguages.push(
                programmingLanguages[checkbox.value.toUpperCase()]);
        }
    });

    return selectedLanguages.length > 0 ? selectedLanguages : null;
}

const disableCommonNotationType = () => {
    const commonNotationType = document.getElementById("checkbox-common-notation-type");
    const simpleNotationType = document.getElementById("checkbox-simple-notation-type");
    commonNotationType.checked = false;
    simpleNotationType.checked = true;
    // hide error about not selected notation after select
    errorDisplayed = false;
    document.getElementById("error-in-center").style.display = "none";
    clearTimeout(mainErrorTimeout);
    clearTimeout(changeOpacityErrorTimeout);
};

const disableSimpleNotationType = () => {
    const commonNotationType = document.getElementById("checkbox-common-notation-type");
    const simpleNotationType = document.getElementById("checkbox-simple-notation-type");
    simpleNotationType.checked = false;
    commonNotationType.checked = true;
    // hide error about not selected notation after select
    errorDisplayed = false;
    document.getElementById("error-in-center").style.display = "none";
    clearTimeout(mainErrorTimeout);
    clearTimeout(changeOpacityErrorTimeout);
}

let errorDisplayed = false;
let mainErrorTimeout, changeOpacityErrorTimeout;

function showError(text, id) {
    if (errorDisplayed === false) {
        errorDisplayed = true
        const error = document.querySelector('#' + id);
        error.innerHTML = `<span class="error">${text}</span>`;
        error.style.display = 'flex';
        mainErrorTimeout = setTimeout(function () {
            $('#' + id).css('opacity', 1);
            error.style.opacity = '0';
        }, 4000);

        $('#' + id).css('opacity', 1);

            error.scrollIntoView({behavior: 'smooth'})

        changeOpacityErrorTimeout = setTimeout(function () {
            error.style.display = 'none';
            errorDisplayed = false;
        }, 5000);
    }
}

const checkboxDownloadCodeCheck = () => {
    clearTimeout(mainErrorTimeout);
    clearTimeout(changeOpacityErrorTimeout);
    const cCodeToDownload = document.getElementById("checkbox-c");
    const javaCodeToDownload = document.getElementById("checkbox-java");
    const pythonCodeToDownload = document.getElementById("checkbox-python");
    if (cCodeToDownload.checked === true ||
        javaCodeToDownload.checked === true ||
        pythonCodeToDownload.checked === true) {
        // hide error about not selected language to download after select
        errorDisplayed = false;
        document.getElementById("error-in-bottom").style.display = "none";
    }
}