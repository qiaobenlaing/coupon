//
//  BMSQ_PhotoViewController.m
//  BMSQC
//
//  Created by liuqin on 15/9/10.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_PhotoViewController.h"
#import "SVProgressHUD.h"
#import "ProctionCollectionViewCell.h"
#import "ENCollectionViewCell.h"
#import "BMSQ_NewBuiltAlbumViewController.h" //新建相册
#import "BMSQ_SubAlbumPhotoViewController.h"
#import "MJRefresh.h"
#import "PhotoScrollView.h"

#import "BMSQ_EditPhotoViewController.h"

#define ORIGINAL_MAX_WIDTH 640.0f

@interface BMSQ_PhotoViewController ()<UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout> {
    
    
    UIView *topView;
    UIView *uploadView;
    
}


@property(nonatomic, strong)NSMutableArray *ProductArray;
@property(nonatomic, strong)NSMutableArray *environmentArray;
@property(nonatomic, strong)UICollectionView *collectionView;


@property(nonatomic, assign)int proPage;  //产品相册页码
@property(nonatomic, assign)int enPage;   //环境相册页码

@property(nonatomic, assign)int currenPage;  //当前相册  1 产品； 2 环境

@end

@implementation BMSQ_PhotoViewController

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    if (self.currenPage ==1) {
        [self loadProdctionPhoto];
        
    }else{
        [self loadenPhoto];
    }
    
    
}

- (void)viewWillDisappear:(BOOL)animated {
    self.navigationController.navigationBarHidden = NO;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
//    [self.navigationItem setTitle:@"商家相册"];
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    [self setNavTitle:@"商家相册"];
    [self setNavBackItem];
    
    [self customRightBtn];

    topView = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 50)];
    topView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:topView];
    
    NSArray *segmentedArray = [[NSArray alloc]initWithObjects:@"产品",@"环境",nil];
    //初始化UISegmentedControl
    UISegmentedControl *segmentedTemp = [[UISegmentedControl alloc]initWithItems:segmentedArray];
    segmentedTemp.frame = CGRectMake(0, 0, APP_VIEW_WIDTH/2, 30.0);
    segmentedTemp.tintColor= [UIColor colorWithRed:182/255.0 green:0/255.0 blue:12/255.0 alpha:1];
    segmentedTemp.center = CGPointMake(APP_VIEW_WIDTH/2, 25);
    
    segmentedTemp.selectedSegmentIndex = 0;
    [segmentedTemp addTarget:self action:@selector(didClicksegmentedControlAction:) forControlEvents:UIControlEventValueChanged];
    [topView addSubview:segmentedTemp];
    


    self.ProductArray = [[NSMutableArray alloc]init];
    self.environmentArray = [[NSMutableArray alloc]init];
    self.proPage = 1;
    self.enPage = 1;
    
    
    uploadView = [[UIView alloc] initWithFrame:CGRectMake(0, topView.frame.origin.y+topView.frame.size.height, APP_VIEW_WIDTH, 50)];
    uploadView.hidden = YES;
    
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
    [self.view addSubview:uploadView];
    
    UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc]init];
    [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
    self.collectionView = [[UICollectionView alloc]initWithFrame:CGRectMake(0, topView.frame.origin.y+topView.frame.size.height, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT) collectionViewLayout:flowLayout];
    self.collectionView.backgroundColor = [UIColor clearColor];
    self.collectionView.delegate = self;
    self.collectionView.dataSource = self;
    self.collectionView.alwaysBounceVertical = YES;
    [self.view addSubview:self.collectionView];
    

    [self.collectionView registerClass:[ProctionCollectionViewCell class] forCellWithReuseIdentifier:@"ProctionCollectionViewCell"];
    [self.collectionView registerClass:[ENCollectionViewCell class] forCellWithReuseIdentifier:@"enCollectionViewCell"];
    
    
    self.currenPage = 1;
    [self loadProdctionPhoto];

}

- (void)customRightBtn //创建右边的按钮
{
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = CGRectMake(APP_VIEW_WIDTH - 64, 20, 64, 44);
    button.tag = 10000;
    [button setImage:[UIImage imageNamed:@"iv_plus"] forState:UIControlStateNormal];
    [button addTarget:self action:@selector(didClickRightButton:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:button];

//    UIBarButtonItem *item = [[UIBarButtonItem alloc]
//                                                             style:UIBarButtonItemStylePlain
//                                                            target:self
//                                                            action:@selector(didClickRightButton:)];
//    item.tintColor = [UIColor whiteColor];
//    self.navigationItem.rightBarButtonItem = item;
//    
}

- (void)didClickRightButton:(UIBarButtonItem *)item {// 新建相册
   
    BMSQ_NewBuiltAlbumViewController *newAlbumVC = [[BMSQ_NewBuiltAlbumViewController alloc] init];
    [self.navigationController pushViewController:newAlbumVC animated:YES];
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


#pragma mark - UICollectionView delegate
-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{

    if (self.currenPage ==1) {
        return self.ProductArray.count;
    }else{
        return self.environmentArray.count;
    }
    
    
}


-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    if (self.currenPage ==1) {
        static NSString *identifer = @"ProctionCollectionViewCell" ;
        ProctionCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:identifer forIndexPath:indexPath];
        
        if(cell == nil){
            cell = [[ProctionCollectionViewCell alloc]init];
        }
        [cell setCollectionCell:[self.ProductArray objectAtIndex:indexPath.row] row:(int)indexPath.row];
        
        
        return cell;
    }else{
        
        static NSString *identifer = @"enCollectionViewCell" ;
        ENCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:identifer forIndexPath:indexPath];
        
        if(cell == nil){
            cell = [[ENCollectionViewCell alloc]init];
        }
        
        
        [cell setEnCell:[self.environmentArray objectAtIndex:indexPath.row] row:(int)indexPath.row];
        return cell;

        
    }
   
    
}

-(CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    if(self.currenPage == 1){
        return CGSizeMake(APP_VIEW_WIDTH/2, APP_VIEW_WIDTH/2-20);
  
    }else{
        return CGSizeMake(APP_VIEW_WIDTH/2, APP_VIEW_WIDTH/2+20);
 
    }
    
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
    
    
    switch (self.currenPage) {
        case 1:
        {
            NSDictionary *dic = [self.ProductArray objectAtIndex:indexPath.row];
            BMSQ_SubAlbumPhotoViewController *vc = [[BMSQ_SubAlbumPhotoViewController alloc]init];
            vc.myTitle = [dic objectForKey:@"name"];
            vc.code = [dic objectForKey:@"code"];
            vc.proDic = dic;
            [self.navigationController pushViewController:vc animated:YES];
            
            
            
         }
            break;
        case 2:{
            
            PhotoScrollView *sc = [[PhotoScrollView alloc]init];
            UIApplication *app = [UIApplication sharedApplication];
            UIWindow *window = app.keyWindow;
            __block PhotoScrollView *_weakPhoto=sc;
            sc.removeSC = ^{
                [_weakPhoto removeFromSuperview];
                
            };
            sc.delegate = self;
            sc.count = (int)self.environmentArray.count;
            [sc setEnImageArray:self.environmentArray];
            [sc setImage:(int)indexPath.row];
            [window addSubview:sc];
            
            
            
            
            
            
        }
            break;
        default:
            break;
    }
   
}





#pragma mark --click--
-(void)didClicksegmentedControlAction:(id)sender{ //顶部按钮的点击
    
    UISegmentedControl *control = (UISegmentedControl *)sender;
    int tag = (int)control.selectedSegmentIndex;
    UIButton *button = [self.view viewWithTag:10000];
    switch (tag) {
        case 0:   //产品
        {
            
            uploadView.hidden = YES;
            self.collectionView.frame = CGRectMake(0, topView.frame.origin.y+topView.frame.size.height, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT);
            self.currenPage = 1;
            
            
            button.hidden = NO;

            
            [self.collectionView reloadData];
            
            
            
            
        }
            break;
        case 1:  //环境
        {
            
            uploadView.hidden = NO;
            
            self.currenPage = 2;
            self.collectionView.frame = CGRectMake(0, topView.frame.origin.y+topView.frame.size.height+uploadView.frame.size.height, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT-uploadView.frame.size.height);
            [self.collectionView reloadData];
            
            if (self.environmentArray.count ==0) {
                [self loadenPhoto];
            }
            

            button.hidden = YES;
            
            [self.collectionView reloadData];
        }
            break;
            
        default:
            break;
    }
    
    
}



-(void)footerRefreshing{
    
//    [self.collectionView footerBeginRefreshing];

    if (self.currenPage ==1) {
        [self loadProdctionPhoto];
        
    }else{
        [self loadenPhoto];
    }
    
}

-(void)loadProdctionPhoto{
    
    [SVProgressHUD showWithStatus:@"正在加载"];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[NSString stringWithFormat:@"%d",self.proPage] forKey:@"page"];

    
    NSString* vcode = [gloabFunction getSign:@"GetShopProductAlbum" strParams:self.shopCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"GetShopProductAlbum" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        

        if ([responseObject isKindOfClass:[NSArray class]]) {
            
            wself.ProductArray = responseObject;

        } else {
            CSAlert(@"数据异常");
        }
        [wself.collectionView reloadData];
        
//        [SVProgressHUD showSuccessWithStatus:@"加载成功"];
        [SVProgressHUD dismiss];

//        [self.collectionView footerEndRefreshing];

        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {\
        [SVProgressHUD showErrorWithStatus:@"加载失败"];
        NSLog(@"请求错误");
//        [self.collectionView footerEndRefreshing];

        
    }];

    
}

//环境相册
-(void)loadenPhoto{
    
    [SVProgressHUD showWithStatus:@"正在加载"];
    [self initJsonPrcClient:@"1"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[NSString stringWithFormat:@"%d",self.enPage] forKey:@"page"];
    
    
    NSString* vcode = [gloabFunction getSign:@"GetShopDecoration" strParams:self.shopCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"GetShopDecoration" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        if ([responseObject isKindOfClass:[NSArray class]]) {
            wself.environmentArray = responseObject;
            
        }else {
            wself.environmentArray = nil;
        }
 
        [wself.collectionView reloadData];
        
        [SVProgressHUD dismiss];
        
//        [self.collectionView footerEndRefreshing];

    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {\
        [SVProgressHUD showErrorWithStatus:@"加载失败"];
        NSLog(@"请求错误");
        
//        [self.collectionView footerEndRefreshing];

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
    }
}

#pragma mark - UIImagePickerController delegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    
    [picker dismissViewControllerAnimated:YES completion:^(){
        UIImage *portraitImg = [info objectForKey:@"UIImagePickerControllerOriginalImage"];
        
        NSLog(@"%@",info);
        
        BMSQ_EditPhotoViewController *editPhotoVC = [[BMSQ_EditPhotoViewController alloc] init];
        editPhotoVC.image = portraitImg;
        editPhotoVC.isProduct = NO;
        
        [self presentViewController:editPhotoVC animated:YES completion:^{
            [editPhotoVC setNavTitle:@"传图片"];
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
        NSLog(@"编辑环境图片");
        
        BMSQ_EditPhotoViewController *editVC = [[BMSQ_EditPhotoViewController alloc] init];
        editVC.isProduct = NO;
        editVC.editDic = button.object;
        [self presentViewController:editVC animated:YES completion:^{
            [editVC setNavTitle:@"修改图片信息"];
        }];
        
    }
    
    NSLog(@"button%@",button);
    
}

//删除环境的图片
- (void)delShopDec:(UIButtonEx *)btnEX {
    
    [SVProgressHUD showWithStatus:@"正在加载"];
    [self initJsonPrcClient:@"1"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[NSString stringWithFormat:@"%@",[btnEX.object objectForKey:@"decorationCode"]] forKey:@"decorationCode"];

    NSString* vcode = [gloabFunction getSign:@"delShopDec" strParams:[NSString stringWithFormat:@"%@",[btnEX.object objectForKey:@"decorationCode"]]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];

    [self.jsonPrcClient invokeMethod:@"delShopDec" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        if (responseObject) {
            NSString *code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"] ];
            if (code.intValue == 50000) {
                
                [self loadenPhoto];
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
