//
//  DecorationShopViewController.m
//  BMSQS
//
//  Created by lxm on 15/8/1.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "DecorationShopViewController.h"
#import "AFURLSessionManager.h"
#import "DAProgressOverlayView.h"
#import "VPImageCropperViewController.h"
#import "AFURLResponseSerialization.h"
#import "AFURLSessionManager.h"

#define ORIGINAL_MAX_WIDTH 640.0f

@interface DecorationShopViewController ()

@end

@implementation DecorationShopViewController
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization.
        self.hidesBottomBarWhenPushed = YES;
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [UINavigationBar appearance].barTintColor =[UIColor colorWithRed:188/255.f green:10/255.f blue:23/255.f alpha:1];
    [[UINavigationBar appearance] setTitleTextAttributes:@{NSFontAttributeName:[UIFont boldSystemFontOfSize:18.f],NSForegroundColorAttributeName: [UIColor whiteColor],}];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self.navigationItem setTitle:@"商铺装修"];
    [self customRightBtn];
    [self httpRequest];
    if(!_dataArray)
        _dataArray = [[NSMutableArray alloc] init];
    if(!_inputArray)
        _inputArray = [[NSMutableArray alloc] init];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (void)viewWillDisappear:(BOOL)animated
{
    //[self.navigationController setNavigationBarHidden:YES animated:YES];
    [super viewWillDisappear:animated];
}

- (void)viewWillAppear:(BOOL)animated
{
    //[self.navigationController setNavigationBarHidden:NO animated:YES];
    [super viewWillAppear:animated];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
#pragma mark -CustomView
- (void)customRightBtn
{
    UIBarButtonItem *item = [[UIBarButtonItem alloc] initWithTitle:@"提交" style:UIBarButtonItemStylePlain target:self action:@selector(setupClicked) ];
    [item setTitleTextAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:16.f],NSForegroundColorAttributeName: [UIColor whiteColor],} forState:UIControlStateNormal];
    self.navigationItem.rightBarButtonItem = item;
    
}

- (void)setupClicked
{
    [self httpCommitRequest];
}

- (void)topImgBtnClicked:(UIButton *)sender
{
    _currentIndexB = sender;
    _isTypeAddTopImage = YES;
    [self editPortrait];

}

- (void)imgUpdateBtnClicked:(EGOImageButton *)sender
{
    _isAdd = NO;
    _currentIndex = sender;
    _isTypeAddTopImage = NO;
    [self editPortrait];
}

- (void)imgAddBtnClicked:(EGOImageButton *)sender
{
    _isAdd = YES;
    _currentIndex = sender;
    _isTypeAddTopImage = NO;
    [self editPortrait];
}

- (void)editPortrait {
    UIActionSheet *choiceSheet = [[UIActionSheet alloc] initWithTitle:nil
                                                             delegate:self
                                                    cancelButtonTitle:@"取消"
                                               destructiveButtonTitle:nil
                                                    otherButtonTitles:@"拍照", @"从相册中选取", nil];
    choiceSheet.tag = 800;
    [choiceSheet showInView:self.view];
}


-(void)btnLong:(UILongPressGestureRecognizer *)gestureRecognizer
{
    _delIndex = gestureRecognizer.view.tag-200;
    if ([gestureRecognizer state] == UIGestureRecognizerStateBegan) {
        UIActionSheet *choiceSheet = [[UIActionSheet alloc] initWithTitle:nil
                                                                 delegate:self
                                                        cancelButtonTitle:@"取消"
                                                   destructiveButtonTitle:nil
                                                        otherButtonTitles:@"设置成形象图片", @"删除", nil];
        choiceSheet.tag = 900;
        [choiceSheet showInView:self.view];
        
    }
}

#pragma mark -Delegate

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return 3;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if(indexPath.row==0)
        return 210.f;
    else if(indexPath.row==1)
        return 200.f;
    return 170.f;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *cellIdentifier = nil;
    if(indexPath.row==0)
        cellIdentifier = @"TopTableViewCell";
    else if(indexPath.row==1)
        cellIdentifier = @"ShortTableViewCell";
    else if(indexPath.row==2)
        cellIdentifier = @"AddImageTableViewCell";
    UITableViewCell *setCell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (setCell == nil) {
        setCell = [[UITableViewCell alloc] initWithStyle:0 reuseIdentifier:cellIdentifier];
        
    }else{
        
    }
//    NSArray *array = [_dataDic objectForKey:@"decoration"];
    NSArray *array = _dataArray;
    if(indexPath.row==0){
        BOOL isHasTop = '\0';
        for(NSDictionary *dic in array){
            
            if([[dic objectForKey:@"type"] intValue]==1){
                isHasTop = YES;
                NSString *imgStr = [NSString stringWithFormat:@"%@/%@",APP_SERVERCE_HOME,[dic objectForKey:@"imgUrl"]];
                self.portraitImageView = (EGOImageView *)[setCell viewWithTag:100];
                self.portraitImageView.imageURL = [NSURL URLWithString:imgStr];
                UIButton *btn = (UIButton *)[setCell viewWithTag:101];
                [btn addTarget:self action:@selector(topImgBtnClicked:) forControlEvents:UIControlEventTouchUpInside];
            }
        }
        if(!isHasTop){
            self.portraitImageView = (EGOImageView *)[setCell viewWithTag:100];
            self.portraitImageView.imageURL = nil;
        }

        UIButton *btn = (UIButton *)[setCell viewWithTag:101];
        [btn addTarget:self action:@selector(topImgBtnClicked:) forControlEvents:UIControlEventTouchUpInside];

    }else if(indexPath.row==1){
        _textView = (UITextView *)[setCell viewWithTag:102];
//        _textView.text = [_dataDic objectForKey:@"shortDes"];
        _textView.text = @"";
        _textView.delegate = self;
        _textLabel = (UILabel *)[setCell viewWithTag:103];
        
//        _textLabel.text = [NSString stringWithFormat:@"%d/200",((NSString *)[_dataDic objectForKey:@"shortDes"]).length];
    }else if(indexPath.row==2){
        if(array.count!=0){
            int i=1;
            for(NSDictionary *dic in array){
                NSString *imgStr = [NSString stringWithFormat:@"%@/%@",APP_SERVERCE_HOME,[dic objectForKey:@"imgUrl"]];
                EGOImageButton *btn = (EGOImageButton *)[setCell viewWithTag:200+i];
                btn.imageURL = [NSURL URLWithString:imgStr];
                btn.delegate = self;
                btn.placeholderImage = [UIImage imageNamed:@"MyBusiness_AddImage"];
                [btn addTarget:self action:@selector(imgUpdateBtnClicked:) forControlEvents:UIControlEventTouchUpInside];
                UILongPressGestureRecognizer *longPress = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(btnLong:)];
                longPress.minimumPressDuration = 0.8;
                [btn addGestureRecognizer:longPress];

                i++;
            }
        }
        int z=array.count+1;
        for(int j=6;j>array.count;j--){
            EGOImageButton *btn = (EGOImageButton *)[setCell viewWithTag:200+z];
            btn.delegate = self;
            btn.imageURL = nil;
            btn.placeholderImage = [UIImage imageNamed:@"MyBusiness_AddImage"];
            [btn addTarget:self action:@selector(imgAddBtnClicked:) forControlEvents:UIControlEventTouchUpInside];
            z++;
        }
        [setCell setBackgroundColor:[UIColor clearColor]];
    }

    setCell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    return setCell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 80)];
    v.backgroundColor = [UIColor clearColor];
    return v;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section;
{
    return 100.f;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section;
{
    return 0.1f;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 9)];
    v.backgroundColor = [UIColor clearColor];
    return v;
}

#pragma mark - HTTP
- (void)httpRequest
{
    NSString* vcode = [gloabFunction getSign:@"getShopDecoration" strParams:[gloabFunction getShopCode]];
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    NSDictionary *dic = @{@"shopCode":[gloabFunction getShopCode],
                          @"tokenCode":[gloabFunction getUserToken],
                          @"vcode":vcode,
                          @"reqtime":[gloabFunction getTimestamp]};
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self.jsonPrcClient invokeMethod:@"getShopDecoration" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        if(IsNOTNullOrEmptyOfArray(responseObject)){
            //_dataDic = responseObject;
            _dataArray = responseObject;
            [_tableView reloadData];
            [self.view bringSubviewToFront:_tableView];
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        CSAlert(error.localizedDescription);
    }];
}

/**
 *  @author lxm, 15-08-02 16:08:03
 *
 *  图片上传接口
 */
- (void)httpImageRequest:(UIImage *)image withObj:(NSObject *)object withIndex:(int)index
{
    NSData *dataObj = UIImageJPEGRepresentation(image, 1.0);
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    /**
     *  @author lxm, 15-08-02 18:08:58
     *
     */
    NSMutableURLRequest *request = [[AFHTTPRequestSerializer serializer] multipartFormRequestWithMethod:@"POST" URLString:@"http://baomi.suanzi.cn/Api/Comm/uploadImg" parameters:nil constructingBodyWithBlock:^(id<AFMultipartFormData> formData) {
        [formData appendPartWithFileData:dataObj name:@"imagefile" fileName:@"Icon.png" mimeType:@"image/png"];
//        NSInputStream *aa = [[NSInputStream alloc] initWithData:dataObj];
//        [formData appendPartWithInputStream:aa name:@"imagefile" fileName:@"imagefile.png" length:dataObj.length mimeType:@"image/png"];
        
    } error:nil];

    NSProgress *progressData = [NSProgress progressWithTotalUnitCount:dataObj.length];
    
    NSURLSessionConfiguration *configuration = [NSURLSessionConfiguration defaultSessionConfiguration];
    AFURLSessionManager *manager = [[AFURLSessionManager alloc] initWithSessionConfiguration:configuration];
    AFHTTPResponseSerializer *aa= [AFJSONResponseSerializer serializer];
    aa.acceptableContentTypes = [NSSet setWithObjects:@"application/json",@"text/json",@"text/javascript",@"text/html",@"text/plain",nil];
    manager.responseSerializer = aa;

    NSURLSessionUploadTask *uploadTask = [manager uploadTaskWithStreamedRequest:request progress:&progressData completionHandler:^(NSURLResponse *response, id responseObject, NSError *error) {
        if (error) {
            NSString *aString = [[NSString alloc] initWithData:[error.userInfo objectForKey:@"com.alamofire.serialization.response.error.data"] encoding:NSUTF8StringEncoding];
            NSLog(@"Error: %@",aString );

        } else {
            NSLog(@"Success: %@ %@", response, responseObject);
            [(DAProgressOverlayView *)object removeFromSuperview];
            NSString *urlStr = nil;
            if([responseObject objectForKey:@"code"]){
                if(index==101){
                    urlStr = [responseObject objectForKey:@"code"];
                    //for(NSDictionary *dic in [_dataDic objectForKey:@"decoration"]){
                    for(NSDictionary *dic in _dataArray){

                        if([[dic objectForKey:@"type"] intValue]==1){
                            _isAdd=NO;
                            [self httpUpdateImageDecoration:[dic objectForKey:@"decorationCode"] withType:@"1" withPic:urlStr];
                            break;
                        }
                        _isAdd = YES;
                    }
                    if(_isAdd){
                        [self httpAddImageDecoration:@"1" withPic:urlStr];
                    }
                }else {
                    if(_isAdd){
                        urlStr = [responseObject objectForKey:@"code"];
                        [self httpAddImageDecoration:@"0" withPic:urlStr];
                    }else{
                        urlStr = [responseObject objectForKey:@"code"];
                        //NSArray *array = [_dataDic objectForKey:@"decoration"];
                        NSArray *array = _dataArray;
                        NSDictionary *dicTemp = [array objectAtIndex:index-201];
                        NSDictionary *dic = @{@"decorationCode":[dicTemp objectForKey:@"decorationCode"],
                                              @"imgUrl":urlStr};
                        [_inputArray addObject:dic];
                        
                        [self httpUpdateImageDecoration:[dicTemp objectForKey:@"decorationCode"] withType:@"0" withPic:urlStr];
                    }
                }
            }

        
        }
        
    }];
    
    [uploadTask resume];
    [progressData addObserver:object
                   forKeyPath:@"fractionCompleted"
                      options:NSKeyValueObservingOptionNew
                      context:NULL];
    
}

- (void)httpAddImageDecoration:(NSString *)typeStr withPic:(NSString *)picStr
{
    
    NSString* vcode = [gloabFunction getSign:@"addShopDecImg" strParams:[gloabFunction getShopCode]];
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    NSDictionary *dic = @{@"shopCode":[gloabFunction getShopCode],
                          @"tokenCode":[gloabFunction getUserToken],
                          @"vcode":vcode,
                          @"reqtime":[gloabFunction getTimestamp],
                          @"type":typeStr,
                          @"imgUrl":picStr};
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"addShopDecImg" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        if(IsNOTNullOrEmptyOfDictionary(responseObject)){
            if([[responseObject objectForKey:@"code"] intValue]==50000){
            }
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        CSAlert(error.localizedDescription);
    }];
}

- (void)httpUpdateImageDecoration:(NSString *)codeStr withType:(NSString *)typeStr withPic:(NSString *)picStr
{
    
    NSString* vcode = [gloabFunction getSign:@"updateShopDecoration" strParams:codeStr];
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    NSDictionary *dic = @{@"shopCode":[gloabFunction getShopCode],
                          @"tokenCode":[gloabFunction getUserToken],
                          @"vcode":vcode,
                          @"reqtime":[gloabFunction getTimestamp],
                          @"decorationCode":codeStr,
                          @"imgUrl":picStr,
                          @"type":typeStr};
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"updateShopDecoration" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        if(IsNOTNullOrEmptyOfDictionary(responseObject)){
            if([[responseObject objectForKey:@"code"] intValue]==50000){
            }
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        CSAlert(error.localizedDescription);
    }];
}

- (void)mainShopDecImg:(NSString *)codeStr
{
    NSString* vcode = [gloabFunction getSign:@"setMainShopDecImg" strParams:[gloabFunction getShopCode]];
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    NSDictionary *dic = @{@"shopCode":[gloabFunction getShopCode],
                          @"tokenCode":[gloabFunction getUserToken],
                          @"vcode":vcode,
                          @"reqtime":[gloabFunction getTimestamp],
                          @"decorationCode":codeStr
                          };
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"setMainShopDecImg" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        if(IsNOTNullOrEmptyOfDictionary(responseObject)){
            if([[responseObject objectForKey:@"code"] intValue]==50000){
                [self httpRequest];
            }
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        CSAlert(error.localizedDescription);
    }];
}


- (void)delDecoration:(NSString *)codeStr
{
    NSString* vcode = [gloabFunction getSign:@"delShopDec" strParams:codeStr];
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    NSDictionary *dic = @{@"shopCode":[gloabFunction getShopCode],
                          @"tokenCode":[gloabFunction getUserToken],
                          @"vcode":vcode,
                          @"reqtime":[gloabFunction getTimestamp],
                          @"decorationCode":codeStr
                          };
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"delShopDec" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        if(IsNOTNullOrEmptyOfDictionary(responseObject)){
            if([[responseObject objectForKey:@"code"] intValue]==50000){
                [self httpRequest];
            }
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        CSAlert(error.localizedDescription);
    }];
}

- (void)httpUpdateShop:(NSString *)codeStr withPic:(NSString *)picStr
{
    
    NSString* vcode = [gloabFunction getSign:@"updateShopDecoration" strParams:codeStr];
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    NSDictionary *dic = @{@"shopCode":[gloabFunction getShopCode],
                          @"tokenCode":[gloabFunction getUserToken],
                          @"vcode":vcode,
                          @"reqtime":[gloabFunction getTimestamp],
                          @"decorationCode":codeStr,
                          @"imgUrl":picStr};
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"updateShopDecoration" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        if(IsNOTNullOrEmptyOfDictionary(responseObject)){
            if([[responseObject objectForKey:@"code"] intValue]==50000){
            }
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        CSAlert(error.localizedDescription);
    }];
}

- (void)httpCommitRequest
{
    
    [self.view endEditing:YES];
    NSString *userStr = [_textView.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(!userStr||userStr.length==0||userStr.length>200){
        CSAlert(@"请输入店铺简介(200字)");
        return;
    }
    
    NSString* vcode = [gloabFunction getSign:@"updateShopShortDes" strParams:[gloabFunction getShopCode]];
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    NSDictionary *dic = @{@"shopCode":[gloabFunction getShopCode],
                          @"tokenCode":[gloabFunction getUserToken],
                          @"vcode":vcode,
                          @"reqtime":[gloabFunction getTimestamp],
                          @"shortDes":userStr};
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self.jsonPrcClient invokeMethod:@"updateShopShortDes" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        if(IsNOTNullOrEmptyOfDictionary(responseObject)){
            if([[responseObject objectForKey:@"code"] intValue]==50000){
                CSAlert(@"修改成功");
                [self.navigationController popViewControllerAnimated:YES];
            }
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        CSAlert(error.localizedDescription);
    }];
}


- (void)textViewDidChange:(UITextView *)textView {
    NSInteger number = [textView.text length];
    if (number > 200) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"提示" message:@"字符个数不能大于200" delegate:self cancelButtonTitle:@"确定" otherButtonTitles:nil];
        [alert show];
        textView.text = [textView.text substringToIndex:200];
        number = 200;
    }
    _textLabel.text = [NSString stringWithFormat:@"%d/200",number];
}

- (void)imageButtonLoadedImage:(EGOImageButton*)imageButton
{
    [((UIView *)[imageButton.superview viewWithTag:imageButton.tag+1000]) removeFromSuperview];
}
- (void)imageButtonFailedToLoadImage:(EGOImageButton*)imageButton error:(NSError*)error
{
    [((UIView *)[imageButton.superview viewWithTag:imageButton.tag+1000]) removeFromSuperview];
}

#pragma mark VPImageCropperDelegate
- (void)imageCropper:(VPImageCropperViewController *)cropperViewController didFinished:(UIImage *)editedImage {
    self.portraitImageView.image = editedImage;
    [cropperViewController dismissViewControllerAnimated:YES completion:^{
        // TO DO
        
        if(_isTypeAddTopImage){
            DAProgressOverlayView *progressOverlayView = [[DAProgressOverlayView alloc] initWithFrame:self.portraitImageView.frame];
            progressOverlayView.tag = _currentIndexB.tag+1000;
            progressOverlayView.overlayColor = [UIColor colorWithRed:0.3 green:0.3 blue:0.3 alpha:0.5];
            progressOverlayView.outerRadiusRatio = 0.7;
            progressOverlayView.innerRadiusRatio = 0.5;
            [self.portraitImageView.superview addSubview:progressOverlayView];
            [self.portraitImageView.superview bringSubviewToFront:progressOverlayView];
            [progressOverlayView displayOperationWillTriggerAnimation];
            [self httpImageRequest:editedImage withObj:progressOverlayView withIndex:_currentIndexB.tag];
        }else{
            DAProgressOverlayView *progressOverlayView = [[DAProgressOverlayView alloc] initWithFrame:_currentIndex.frame];
            progressOverlayView.tag = _currentIndex.tag+1000;
            progressOverlayView.overlayColor = [UIColor colorWithRed:0.3 green:0.3 blue:0.3 alpha:0.5];
            progressOverlayView.outerRadiusRatio = 0.7;
            progressOverlayView.innerRadiusRatio = 0.5;
            [_currentIndex.superview addSubview:progressOverlayView];
            [_currentIndex.superview bringSubviewToFront:progressOverlayView];
            [progressOverlayView displayOperationWillTriggerAnimation];
            _currentIndex.placeholderImage = editedImage;
            _currentIndex.imageURL = nil;
            [self httpImageRequest:editedImage withObj:progressOverlayView withIndex:_currentIndex.tag];
        }
        


    }];
}

- (void)imageCropperDidCancel:(VPImageCropperViewController *)cropperViewController {
    [cropperViewController dismissViewControllerAnimated:YES completion:^{
    }];
}

#pragma mark UIActionSheetDelegate
- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    if(actionSheet.tag==800){
        if (buttonIndex == 0) {
            // 拍照
            if ([self isCameraAvailable] && [self doesCameraSupportTakingPhotos]) {
                UIImagePickerController *controller = [[UIImagePickerController alloc] init];
                controller.sourceType = UIImagePickerControllerSourceTypeCamera;
                if ([self isFrontCameraAvailable]) {
                    controller.cameraDevice = UIImagePickerControllerCameraDeviceFront;
                }
                NSMutableArray *mediaTypes = [[NSMutableArray alloc] init];
                [mediaTypes addObject:(__bridge NSString *)kUTTypeImage];
                controller.mediaTypes = mediaTypes;
                controller.delegate = self;
                [self presentViewController:controller
                                   animated:YES
                                 completion:^(void){
                                     NSLog(@"Picker View Controller is presented");
                                 }];
            }
            
        } else if (buttonIndex == 1) {
            // 从相册中选取
            if ([self isPhotoLibraryAvailable]) {
                UIImagePickerController *controller = [[UIImagePickerController alloc] init];
                controller.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
                NSMutableArray *mediaTypes = [[NSMutableArray alloc] init];
                [mediaTypes addObject:(__bridge NSString *)kUTTypeImage];
                controller.mediaTypes = mediaTypes;
                controller.delegate = self;
                [self presentViewController:controller
                                   animated:YES
                                 completion:^(void){
                                     NSLog(@"Picker View Controller is presented");
                                 }];
            }
        }
    }else if (actionSheet.tag==900){
        if (buttonIndex == 0) {
            //设置封面
//            NSArray *array = [_dataDic objectForKey:@"decoration"];
            NSArray *array = _dataArray;
            if(IsNOTNullOrEmptyOfArray(array)){
                [self mainShopDecImg:array[_delIndex-1][@"decorationCode"]];
            }
        }else if(buttonIndex == 1){
            //删除
            
//            NSArray *array = [_dataDic objectForKey:@"decoration"];
            NSArray *array = _dataArray;
            if(IsNOTNullOrEmptyOfArray(array)){
                [self delDecoration:array[_delIndex-1][@"decorationCode"]];
            }
        }
    }
}
#pragma mark image scale utility
- (UIImage *)imageByScalingToMaxSize:(UIImage *)sourceImage {
    if (sourceImage.size.width < ORIGINAL_MAX_WIDTH) return sourceImage;
    CGFloat btWidth = 0.0f;
    CGFloat btHeight = 0.0f;
    if (sourceImage.size.width > sourceImage.size.height) {
        btHeight = ORIGINAL_MAX_WIDTH;
        btWidth = sourceImage.size.width * (ORIGINAL_MAX_WIDTH / sourceImage.size.height);
    } else {
        btWidth = ORIGINAL_MAX_WIDTH;
        btHeight = sourceImage.size.height * (ORIGINAL_MAX_WIDTH / sourceImage.size.width);
    }
    CGSize targetSize = CGSizeMake(btWidth, btHeight);
    return [self imageByScalingAndCroppingForSourceImage:sourceImage targetSize:targetSize];
}

- (UIImage *)imageByScalingAndCroppingForSourceImage:(UIImage *)sourceImage targetSize:(CGSize)targetSize {
    UIImage *newImage = nil;
    CGSize imageSize = sourceImage.size;
    CGFloat width = imageSize.width;
    CGFloat height = imageSize.height;
    CGFloat targetWidth = targetSize.width;
    CGFloat targetHeight = targetSize.height;
    CGFloat scaleFactor = 0.0;
    CGFloat scaledWidth = targetWidth;
    CGFloat scaledHeight = targetHeight;
    CGPoint thumbnailPoint = CGPointMake(0.0,0.0);
    if (CGSizeEqualToSize(imageSize, targetSize) == NO)
    {
        CGFloat widthFactor = targetWidth / width;
        CGFloat heightFactor = targetHeight / height;
        
        if (widthFactor > heightFactor)
            scaleFactor = widthFactor; // scale to fit height
        else
            scaleFactor = heightFactor; // scale to fit width
        scaledWidth  = width * scaleFactor;
        scaledHeight = height * scaleFactor;
        
        // center the image
        if (widthFactor > heightFactor)
        {
            thumbnailPoint.y = (targetHeight - scaledHeight) * 0.5;
        }
        else
            if (widthFactor < heightFactor)
            {
                thumbnailPoint.x = (targetWidth - scaledWidth) * 0.5;
            }
    }
    UIGraphicsBeginImageContext(targetSize); // this will crop
    CGRect thumbnailRect = CGRectZero;
    thumbnailRect.origin = thumbnailPoint;
    thumbnailRect.size.width  = scaledWidth;
    thumbnailRect.size.height = scaledHeight;
    
    [sourceImage drawInRect:thumbnailRect];
    
    newImage = UIGraphicsGetImageFromCurrentImageContext();
    if(newImage == nil) NSLog(@"could not scale image");
    
    //pop the context to get back to the default
    UIGraphicsEndImageContext();
    return newImage;
}

#pragma mark - UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    [picker dismissViewControllerAnimated:YES completion:^() {
        UIImage *portraitImg = [info objectForKey:@"UIImagePickerControllerOriginalImage"];
        //            CGSize aFactSize = [[UIScreen mainScreen] bounds].size;
        portraitImg = [self imageByScalingToMaxSize:portraitImg];
        VPImageCropperViewController *imgCropperVC = [[VPImageCropperViewController alloc] initWithImage:portraitImg cropFrame:CGRectMake(0, 100.0f, self.view.frame.size.width, self.view.frame.size.width*9/16) limitScaleRatio:3.0];
        imgCropperVC.delegate = self;
        [self presentViewController:imgCropperVC animated:YES completion:^{
            // TO DO
        }];
    }];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    [picker dismissViewControllerAnimated:YES completion:^(){
    }];
}

- (void)navigationController:(UINavigationController *)navigationController willShowViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    // bug fixes: UIIMagePickerController使用中偷换StatusBar颜色的问题
    if ([navigationController isKindOfClass:[UIImagePickerController class]] &&
        ((UIImagePickerController *)navigationController).sourceType ==     UIImagePickerControllerSourceTypePhotoLibrary) {
        [[UIApplication sharedApplication] setStatusBarHidden:NO];
        [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent animated:NO];
    }
}

#pragma mark camera utility
- (BOOL) isCameraAvailable{
    return [UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera];
}

- (BOOL) isRearCameraAvailable{
    return [UIImagePickerController isCameraDeviceAvailable:UIImagePickerControllerCameraDeviceRear];
}

- (BOOL) isFrontCameraAvailable {
    return [UIImagePickerController isCameraDeviceAvailable:UIImagePickerControllerCameraDeviceFront];
}

- (BOOL) doesCameraSupportTakingPhotos {
    return [self cameraSupportsMedia:(__bridge NSString *)kUTTypeImage sourceType:UIImagePickerControllerSourceTypeCamera];
}

- (BOOL) isPhotoLibraryAvailable{
    return [UIImagePickerController isSourceTypeAvailable:
            UIImagePickerControllerSourceTypePhotoLibrary];
}
- (BOOL) canUserPickVideosFromPhotoLibrary{
    return [self
            cameraSupportsMedia:(__bridge NSString *)kUTTypeMovie sourceType:UIImagePickerControllerSourceTypePhotoLibrary];
}
- (BOOL) canUserPickPhotosFromPhotoLibrary{
    return [self
            cameraSupportsMedia:(__bridge NSString *)kUTTypeImage sourceType:UIImagePickerControllerSourceTypePhotoLibrary];
}

- (BOOL) cameraSupportsMedia:(NSString *)paramMediaType sourceType:(UIImagePickerControllerSourceType)paramSourceType{
    __block BOOL result = NO;
    if ([paramMediaType length] == 0) {
        return NO;
    }
    NSArray *availableMediaTypes = [UIImagePickerController availableMediaTypesForSourceType:paramSourceType];
    [availableMediaTypes enumerateObjectsUsingBlock: ^(id obj, NSUInteger idx, BOOL *stop) {
        NSString *mediaType = (NSString *)obj;
        if ([mediaType isEqualToString:paramMediaType]){
            result = YES;
            *stop= YES;
        }
    }];
    return result;
}
@end
