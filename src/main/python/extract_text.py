import sys
from docx import Document
from PyPDF2 import PdfReader
import os
import subprocess
import tempfile
import aspose.words as aw
from sympy import false


def doc_to_docx(file_path):
    try:
        docx_file_path=file_path.replace(".doc", ".docx")
        doc = aw.Document(file_path)
        doc.save(docx_file_path)
        return docx_file_path
    except Exception as e:
        print(f"doc파일 변환중 오류 발생: {e}", file=sys.stderr)
        return None

def extract_from_docx(file_path):
    """DOCX 파일에서 일반 텍스트와 표 안의 텍스트를 모두 추출"""
    try:
        is_doc = False
        if file_path.endswith('.doc'):
            is_doc = True
            new_file_path = doc_to_docx(file_path)
            if file_path is None:
                return "conversion_error"
        else:
            new_file_path = file_path


        doc = Document(new_file_path)
        text = []

        # 일반 텍스트 추출
        for para in doc.paragraphs:
            if para.text.strip():  # 빈 줄이 아니면 추가
                text.append(para.text)

        # 표 안의 텍스트 추출
        for table in doc.tables:
            for row in table.rows:
                row_text = " | ".join(cell.text.strip() for cell in row.cells if cell.text.strip())
                if row_text:
                    text.append("[표] " + row_text)  # 표 구분자 추가 가능

        if is_doc:
            try:
                if os.path.exists(new_file_path):
                    os.remove(new_file_path)
                    print(f"{new_file_path} 파일이 성공적으로 삭제되었습니다.")
                else:
                    print(f"{new_file_path} 파일이 존재하지 않습니다.")
            except Exception as e:
                print(f"파일 삭제 중 오류 발생: {e}")

        return "\n".join(text) if text else "no_content"
    except Exception as e:
        print(f"DOCX 파일 처리 중 오류 발생: {e}", file=sys.stderr)
        return "pyerror"

def extract_from_pdf(file_path):
    """PDF 파일에서 텍스트 추출"""
    try:
        reader = PdfReader(file_path)
        text = ""
        for page in reader.pages:
            sub = page.extract_text()
            if sub:
                text += sub + "\n"
        return text.strip()
    except Exception as e:
        print(f"PDF 파일 처리 중 오류 발생: {e}", file=sys.stderr)
        return "pyerror"

def extract_from_hwp(file_path):
    """HWP 파일에서 텍스트 추출 (표 구조 보존)"""
    try:
        # 임시 디렉토리 생성
        temp_dir = tempfile.mkdtemp()

        # hwp5html로 변환 - 디렉토리에 저장
        env = os.environ.copy()
        env["PYTHONIOENCODING"] = "utf-8"

        # HTML 파일로 변환
        cmd_convert = ['hwp5html', '--output', temp_dir, file_path]
        result_convert = subprocess.run(
            cmd_convert,
            capture_output=True,
            text=True,
            encoding='utf-8',
            errors='ignore',
            env=env
        )

        if result_convert.returncode == 0:
            # HTML 파일 찾기 (첫 번째 HTML 파일 사용)
            html_files = [f for f in os.listdir(temp_dir) if f.endswith('.html')]
            if html_files:
                html_path = os.path.join(temp_dir, html_files[0])

                try:
                    with open(html_path, 'r', encoding='utf-8', errors='ignore') as f:
                        html_content = f.read()

                    # 표 구조 처리
                    # 테이블 시작 표시
                    html_content = html_content.replace('<table', '\n[표 시작]\n<table')
                    html_content = html_content.replace('</table>', '</table>\n[표 끝]\n')
                    # 행 구분
                    html_content = html_content.replace('</tr>', '</tr>\n')
                    # 셀 구분
                    html_content = html_content.replace('</td>', ' || </td>')
                    html_content = html_content.replace('</th>', ' || </th>')

                    # 단락 구분
                    html_content = html_content.replace('</p>', '</p>\n')
                    html_content = html_content.replace('<br>', '\n')

                    # HTML 태그 제거
                    import re
                    # 스타일 태그 제거
                    html_content = re.sub(r'<style.*?</style>', '', html_content, flags=re.DOTALL)
                    # 나머지 HTML 태그 제거
                    text = re.sub('<[^<]+?>', '', html_content)

                    # 정리
                    # 연속된 구분자 정리
                    text = re.sub(r'\|\|\s+\|\|', '||', text)
                    # 빈 줄 정리
                    text = re.sub(r'\n\s*\n', '\n', text)
                    # 앞뒤 공백 정리
                    text = re.sub(r'^\s+|\s+$', '', text, flags=re.MULTILINE)
                    # 이중 스페이스 제거
                    text = re.sub(r' +', ' ', text)

                    result = text.strip()
                    if result:
                        return result
                except Exception as e:
                    print(f"HWP 파일 처리 중 오류 발생2: {e}", file=sys.stderr)
                    return "pyerror"

        # hwp5html 변환 실패 시 hwp5txt로 시도
        cmd_txt = ['hwp5txt', file_path]
        result_txt = subprocess.run(
            cmd_txt,
            capture_output=True,
            text=True,
            encoding='utf-8',
            errors='ignore',
            env=env
        )

        if result_txt.returncode == 0:
            return result_txt.stdout.strip() if result_txt.stdout.strip() else "pyerror"

        print("모든 변환 방법이 실패했습니다.", file=sys.stderr)
        return "pyerror"

    except Exception as e:
        print(f"HWP 파일 처리 중 오류 발생: {e}", file=sys.stderr)
        return "pyerror"
    finally:
        # 임시 디렉토리 정리
        try:
            import shutil
            if 'temp_dir' in locals():
                shutil.rmtree(temp_dir, ignore_errors=True)
        except:
            pass

def extract(file_type, file_path):
    """파일 유형에 따라 적절한 텍스트 추출 함수 호출"""
    if file_type == "docx"or file_type == "doc":
        return extract_from_docx(file_path)
    elif file_type == "pdf":
        return extract_from_pdf(file_path)
    elif file_type == "hwp":
        return extract_from_hwp(file_path)
    else:
        print(f"지원하지 않는 파일 유형입니다: {file_type}", file=sys.stderr)
        return "pyerror"

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("사용법: python extract_text.py [파일유형] [파일경로]", file=sys.stderr)
        sys.exit(1)

    file_type = sys.argv[1]  # 파일 확장자 (예: pdf, docx, hwp)
    file_path = sys.argv[2]


    #file_type = "doc"
    #file_path = "이력서 및 자기소개서.doc"



    content = extract(file_type, file_path)
    print(content)
    #print(content.encode('utf-8').decode('utf-8'))  # UTF-8 인코딩 강제

