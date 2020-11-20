//
//  BMSQ_SubAlbumPhotoViewController.m
//  BMSQC
//
//  Created by liuqin on 15/9/11.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_SubAlbumPhotoViewController.h"
#import "BMSQ_shopCollectionViewCell.h"
#import "SVProgressHUD.h"
#import "PhotoScrollView.h"

#import "BMSQ_EditPhotoViewController.h"

#import "BMSQ_NewBuiltAlbumViewController.h"


@interface BMSQ_SubAlbumPhotoViewController ()<UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout> {
    UIView *uploadView;
}


@property (nonatomic, strong)NSMutableArray *subPhotoArray;

@property(nonatomic, strong)UICollectionView *collectionView;


@end

@implementation BMSQ_SubAlbumPhotoViewController


- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self loadData];
}

- (void)viewDidLoad {
    [super viewDidLoad];

  
    [self loadData];
    
    [self setViewUp];

}


- (void)setViewUp {
    [self.navigationItem setTitle:_myTitle];
    [self setNavTitle:_myTitle];
    [self setNavBackItem];
    
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    
    
    self.subPhotoArray = [[NSMutableArray alloc]init];
    

    [self setUploadView];//上传按钮
    [self customRightBtn]; //导航栏右边的按钮  编辑
    
    UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc]init];
    [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
    self.collectionView = [[UICollectionView alloc]initWithFrame:CGRectMake(0, uploadView.frame.origin.y+uploadView.frame.size.height, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT) collectionViewLayout:flowLayout];
    self.collectionView.backgroundColor = [UIColor clearColor];
    self.collectionView.delegate = self;
    self.collectionView.dataSource = self;
    [self.view addSubview:self.collectionView];
    
    [self.collectionView registerClass:[BMSQ_shopCollectionViewCell class] forCellWithReuseIdentifier:@"BMSQ_shopCollectionViewCell"];
    
    
}


- (void)customRightBtn {
//    UIBarButtonItem *item = [[UIBarButtonItem alloc] initWithTitle:@"编辑"
//                                                             style:UIBarButtonItemStylePlain
//                                                            target:self
//                                                            action:@selector(didClickRightButton:)];
//    item.tintColor = [UIColor whiteColor];
//    self.navigationItem.rightBarButtonItem = item;
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = CGRectMake(APP_VIEW_WIDTH - 64, 20, 64, 44);
    [button setTitle:@"编辑" forState:UIControlStateNormal];
    [button addTarget:self action:@selector(didClickRightButton:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:button];
    
    
}

//创建上传按钮
- (void)setUploadView {
    
    uploadView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 50)];
    [self.view addSubview:uploadView];
    
    
    UIButton *btn_upLoadPhoto = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    btn_upLoadPhoto.frame = CGRectMake(0, 0, APP_VIEW_WIDTH/2, 30);
    [btn_upLoadPhoto setTitle:@"上传照片" forState:UIControlStateNormal];
    [btn_upLoadPhoto setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
    btn_upLoadPhoto.backgroundColor = [UIColor whiteColor];
    btn_upLoadPhoto.center = CGPointMake(APP_VIEW_WIDTH/2, 25);
    [btn_upLoadPhoto addTarget:self action:@selector(upLoadPhotoAction) forControlEvents:UIControlEventTouchUpInside];
    
    [btn_upLoadPhoto.layer setMasksToBounds:YES];
    [btn_upLoadPhoto.layer setCornerRadius:8.0]; //设置矩圆角半径
    [btn_upLoadPhoto.layer setBorderWidth:1.0];   //边框宽度
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    CGColorRef colorref = CGColorCreate(colorSpace,(CGFloat[]){ 1, 0, 0, 1 });
    [btn_upLoadPhoto.layer setBorderColor:colorref];//边框颜色
    
    [uploadView addSubview:btn_upLoadPhoto];
    
}


-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    
    return self.subPhotoArray.count;
    
    
}


-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    static NSString *identifer = @"BMSQ_shopCollectionViewCell" ;
    BMSQ_shopCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:identifer forIndexPath:indexPath];
    
    if(cell == nil){
        cell = [[BMSQ_shopCollectionViewCell alloc]init];
    }
    
    [cell setSubAlbm:[self.subPhotoArray objectAtIndex:indexPath.row] row:(int)indexPath.row];
    return cell;
    
    
}

-(CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    return CGSizeMake(APP_VIEW_WIDTH/2, APP_VIEW_WIDTH/2);
}
//
-(UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section{
    
    return UIEdgeInsetsMake(0,0,0, 0);
    
}
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section{
    return 0;
}
-(CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section{
    return 0;
}


-(BOOL)collectionView:(UICollectionView *)collectionView shouldSelectItemAtIndexPath:(NSIndexPath *)indexPath{
    return  YES;
}




-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath{
    
    
    PhotoScrollView *sc = [[PhotoScrollView alloc]init];
    UIApplication *app = [UIApplication sharedApplication];
    UIWindow *window = app.keyWindow;
    __block PhotoScrollView *_weakPhoto=sc;
    sc.removeSC = ^{
        [_weakPhoto removeFromSuperview];
        
    };
    sc.delegate = self;
    sc.count = (int)self.subPhotoArray.count;
    [sc setImageDicView:self.subPhotoArray];
    [sc setImage:(int)indexPath.row];
    [window addSubview:sc];
    
    
}




-(void)loadData{
    
    
    
    [SVProgressHUD showWithStatus:@"正在加载"];
    [self initJsonPrcClient:@"1"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[NSString stringWithFormat:@"%@",[self.proDic objectForKey:@"code"]] forKey:@"code"];
    
    
    NSString* vcode = [gloabFunction getSign:@"GetSubAlbumPhoto" strParams:[NSString stringWithFormat:@"%@",[self.proDic objectForKey:@"code"]]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"GetSubAlbumPhoto" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        
        NSArray *arr = responseObject;
        
        if (arr.count > 0) {
            
            wself.subPhotoArray = responseObject;
            

        } else if (arr.count==0) {
            
            
            wself.subPhotoArray = nil;
            
        }
        [wself.collectionView reloadData];
        [SVProgressHUD dismiss];
    
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {\
        [SVProgressHUD showErrorWithStatus:@"加载失败"];
        NSLog(@"请求错误");

    }];

}


#pragma mark - 点击事件
- (void)didClickRightButton:(UIButton *)btn {
    NSLog(@"编辑");
    BMSQ_NewBuiltAlbumViewController *vc = [[BMSQ_NewBuiltAlbumViewController alloc] init];
    vc.isEdit = YES;
    vc.albumDic = _proDic;
    [self.navigationController pushViewController:vc animated:YES];
    
    
}

- (void)upLoadPhotoAction { //上传照片 按钮事件
    
    NSLog(@"上传照片");
    UIActionSheet *choiceSheet = [[UIActionSheet alloc] initWithTitle:nil
                                                             delegate:self
                                                    cancelButtonTitle:@"取消"
                                               destructiveButtonTitle:nil
                                                    otherButtonTitles:@"拍照", @"从相册中选取", nil];
    choiceSheet.tag = 800;
    [choiceSheet showInView:self.view];
    
    
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
    }
}

#pragma mark - UIImagePickerController delegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary*)info {
    
    [picker dismissViewControllerAnimated:YES completion:^(){
        UIImage *portraitImg = [info objectForKey:@"UIImagePickerControllerOriginalImage"];
        
        BMSQ_EditPhotoViewController *editPhotoVC = [[BMSQ_EditPhotoViewController alloc] init];
        editPhotoVC.isProduct = YES;
        editPhotoVC.image = portraitImg;
        editPhotoVC.dic = self.proDic;
        editPhotoVC.productName = self.myTitle;
        [self presentViewController:editPhotoVC animated:YES completion:^{
            
            [editPhotoVC setNavTitle:@"传照片"];
            
        }];

    }];
    
}
- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    
    [picker dismissViewControllerAnimated:YES completion:^(){
    }];
    
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


#pragma mark - PhotoScroll delegate
- (void)PhotoScrollbtnAction:(UIButtonEx *)button {
    
    if (button.tag == 1001) {//删除
        [self delShopDec:button];
        
    }else if (button.tag == 1002) {//编辑
        NSLog(@"编辑产品图片");
        
        BMSQ_EditPhotoViewController *editVC = [[BMSQ_EditPhotoViewController alloc] init];
        editVC.isProduct = YES;
        editVC.editDic = button.object;
        [self presentViewController:editVC animated:YES completion:^{
            [editVC setNavTitle:@"修改图片信息"];
        }];
        
    }
    
}

//删除环境的图片
- (void)delShopDec:(UIButtonEx *)btnEX {
    
    [SVProgressHUD showWithStatus:@"正在加载"];
    [self initJsonPrcClient:@"1"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[NSString stringWithFormat:@"%@",[btnEX.object objectForKey:@"code"]] forKey:@"code"];
    
    NSString* vcode = [gloabFunction getSign:@"delSubAlbumPhoto" strParams:[NSString stringWithFormat:@"%@",[btnEX.object objectForKey:@"code"]]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"delSubAlbumPhoto" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        if (responseObject) {
            NSString *code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"] ];
            if (code.intValue == 50000) {
                
                [self loadData];
                [SVProgressHUD showSuccessWithStatus:@"已删除"];
                //                [self remo]
            } else if (code.intValue == 20000) {
                CSAlert(@"失败,请重试");
            }
            
        }
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {\
        [SVProgressHUD showErrorWithStatus:@"加载失败"];
        NSLog(@"请求错误");
        
    }];
    
    
}



@end
